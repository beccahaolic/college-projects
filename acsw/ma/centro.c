#include <stdio.h>
#include <stdlib.h>

#define IMAGE_WIDTH 320
#define IMAGE_HEIGHT 180

void read_rgb_image(const char *filename, unsigned char *R, unsigned char *G, unsigned char *B) {
    FILE *fp = fopen(filename, "rb");  // open file in binary mode
    if (fp == NULL) {
        printf("Error: Unable to open file\n");
        exit(1);
    }
    // Read the image into memory
    unsigned char temp[IMAGE_HEIGHT*IMAGE_WIDTH*3];
    fread(temp, sizeof(unsigned char), IMAGE_HEIGHT*IMAGE_WIDTH*3, fp);
    fclose(fp);
    // Copy the image data to the output matrices
    for (int i = 0; i < IMAGE_HEIGHT*IMAGE_WIDTH; ++i) {
        R[i] = temp[3*i];
        G[i] = temp[3*i+1];
        B[i] = temp[3*i+2];
    }
}
/*void read_rgb_image(const char *filename,unsigned char *R, unsigned char *G, unsigned char *B) {
    char filename[256];
    printf("Enter the filename of the RGB image: ");
    scanf("%s", filename);
    FILE *fp = fopen(filename, "rb");  // open file in binary mode
    if (fp == NULL) {
        printf("Error: Unable to open file\n");
        exit(1);
    }
    // Read the image into memory
    unsigned char temp[IMAGE_HEIGHT*IMAGE_WIDTH*3];
    fread(temp, sizeof(unsigned char), IMAGE_HEIGHT*IMAGE_WIDTH*3, fp);
    fclose(fp);
    // Copy the image data to the output matrices
    for (int i = 0; i < IMAGE_HEIGHT*IMAGE_WIDTH; ++i) {
        R[i] = temp[3*i];
        G[i] = temp[3*i+1];
        B[i] = temp[3*i+2];
    }
}
*/
void hue_image(const char* output_file,unsigned char *R, unsigned char *G, unsigned char *B)
{
    int HUE=0;
    float X;
    int index = 0;
    // Open the output file for writing
    FILE* f = fopen(output_file, "w");
    if (f == NULL) {
        fprintf(stderr, "Failed to open output file: %s\n", output_file);
        return;
    }
    // Loop through each pixel in the image
    for (int i = 0; i < IMAGE_HEIGHT; i++) {
        for (int j = 0; j < IMAGE_WIDTH; j++) {
            int r = R[index];
            int g = G[index];
            int b = B[index];
            // Convert to HUE
            if (r > g && g >= b) {
                X = (g - b);
                X = X / (r - b);
                HUE = 60 * X;
            } 
            else if (g >= r && r > b) {
                X = (g - r);
                X = X / (g - b);
                HUE = 60 * X + 60;
            } 
            else if (g > b && b >= r) {
                X = (b - r);
                X = X / (g - r);
                HUE = 60 * X + 120;
            } 
            else if (b >= g && g > r) {
                X = (b - g);
                X = X / (b - r);
                HUE = 60 * X + 240;
            } 
            else if (b > r && r >= g) {
                X = (r - g);
                X = X / (b - g);
                HUE = 60 * X + 300;
            } 
            else if (r >= b && b > g) {
                X = (r - b);
                X = X / (r - g);
                HUE = 60 * X + 360;
            }
            // Write the HUE value to the output file
            fprintf(f, "%d ", HUE);
            index++;
        }
        fprintf(f, "\n"); // Add a newline at the end of each row
    }
    fclose(f); // Close the output file
}
int indicator(int character,const char* output_file,unsigned char *R, unsigned char *G, unsigned char *B) {
    
    // Generate the hue image
    hue_image(output_file, R, G, B);

    // Check if each pixel belongs to the specified character
    FILE* f = fopen(output_file, "r");
    if (f == NULL) {
        fprintf(stderr, "Failed to open output file: %s\n", output_file);
        return 0;
    }
    int count = 0;
    for (int i = 0; i < IMAGE_HEIGHT*IMAGE_WIDTH; i++) { 
        int hue;
        fscanf(f, "%d", &hue);
        if (character == 1 && hue >= 40 && hue <= 80) {
            count++;
        } else if (character == 2 && hue >= 1 && hue <= 15) {
            count++;
        } else if (character == 3 && (hue >= 160 && hue <= 180)) {
            count++;
        }
    }
    
    // Close the output file
    fclose(f);

    // Return the count of pixels that match the expected hue range for the character
    return count;
}

void center_of_mass(int character ,const char* output_file, unsigned char *R,  unsigned char *G, unsigned char *B) {
    int N = 0; // total number of pixels in character p
    int x_sum = 0; // sum of x coordinates of pixels in character p
    int y_sum = 0; // sum of y coordinates of pixels in character p
    int cx;
    int cy;
    // Iterate over all pixels in the image
    for (int y = 0; y < IMAGE_HEIGHT; y++) {
        for (int x = 0; x < IMAGE_WIDTH; x++) {
            // Check if pixel (x,y) belongs to character p
            int belongs_to_p = indicator(1, output_file, R, G, B);
            if (belongs_to_p) {
                // Increment counters
                N++;
                x_sum += x;
                y_sum += y;
            }
        }
    }

    // Calculate center of mass
    if (N > 0) {
        cx = x_sum / N;
        cy = y_sum / N;
    } else {
        // No pixels belonging to character p were found, return an invalid value
        cx = 0;
        cy = 0;
    }
}




int main(int argc, char const *argv[]) {
    // Allocate memory for RGB arrays
    unsigned char *R = (unsigned char*)malloc(sizeof(unsigned char) * IMAGE_WIDTH * IMAGE_HEIGHT);
    unsigned char *G = (unsigned char*)malloc(sizeof(unsigned char) * IMAGE_WIDTH * IMAGE_HEIGHT);
    unsigned char *B = (unsigned char*)malloc(sizeof(unsigned char) * IMAGE_WIDTH * IMAGE_HEIGHT);
    
    // Read RGB image
    read_rgb_image("starwars.rgb", R, G, B);
    
    // Generate hue image and check for character indicators
    const char* output_file = "output1.rgb";
    printf("Character indicators:\n");
    printf("Yoda: %d pixels\n", indicator(1, output_file, R, G, B));
    printf("Maul: %d pixels\n", indicator(2, output_file, R, G, B));
    printf("Mando: %d pixels\n", indicator(3, output_file, R, G, B));
    
    // Free memory
    free(R);
    free(G);
    free(B);
    return 0;
}
