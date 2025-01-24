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
void write_rgb_image(const char *ffilename,unsigned char *R, unsigned char *G, unsigned char *B) {
    FILE *fp = fopen(ffilename, "wb");  // open file in binary mode
    if (fp == NULL) {
        printf("Error: Unable to open file\n");
        exit(1);
    }
    // Write the image data to the file
    for (int i = 0; i < IMAGE_HEIGHT*IMAGE_WIDTH; ++i) {
        fputc(R[i], fp);
        fputc(G[i], fp);
        fputc(B[i], fp);
    }
 fclose(fp);}

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
            else 
            printf("error hue\n");
            // Write the HUE value to the output file
            fprintf(f, "%d ", HUE);
            index++;
        }
        fprintf(f, "\n"); // Add a newline at the end of each row
    }
    fclose(f); // Close the output fil
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
    int count = 0; // total number of pixels in character p
    int x_sum = 0; // sum of x coordinates of pixels in character p
    int y_sum = 0; // sum of y coordinates of pixels in character p
    int cx=0;
    int cy=0;
    for (int y = 0; y < IMAGE_HEIGHT; y++) {
        for (int x = 0; x < IMAGE_WIDTH; x++) {
            int hue;
            fscanf(f, "%d", &hue);
            if (character == 1 && hue >= 40 && hue <= 80) {
                count++;
                x_sum += x;
                y_sum += y;
            } else if (character == 2 && hue >= 1 && hue <= 15) {
                count++;
                x_sum += x;
                y_sum += y;
            } else if (character == 3 && (hue >= 160 && hue <= 180)) {
                count++;
                x_sum += x;
                y_sum += y;
            }
        }
    }
    if (count > 0) {
        cx = x_sum / count;
        cy = y_sum / count;
        printf("Center of mass: (%d, %d)\n", cx, cy);
    } else {
        // No pixels belonging to character p were found, return an invalid value
        cx = 0;
        cy = 0;
        printf("No pixels belonging to character %d were found\n", character);
    }
    fclose(f);
    return count;
}

int draw_cross(int character, const char* output_file, unsigned char* R, unsigned char* G, unsigned char* B) {
    indicator(character, output_file, R, G, B);
    FILE* f = fopen(output_file, "r");
    if (f == NULL) {
        fprintf(stderr, "Failed to open output file: %s\n", output_file);
        return 0;
    }
    int cx=0;
    int cy=0;   
    fscanf(f, "%d", &cx);
    fscanf(f, "%d", &cy);
    printf("%d  %d", cx, cy);
    
    fclose(f);
    
    int size = 10;
    int x_start = cx - size/2;
    int x_end = cx + size/2;
    int y_start = cy - size/2;
    int y_end = cy + size/2;
    for (int y = y_start; y <= y_end; y++) {
        int index = y * IMAGE_WIDTH + cx;
        R[index] = 255;
        G[index] = 0;
        B[index] = 0;
    }
    for (int x = x_start; x <= x_end; x++) {
        int index = cy * IMAGE_WIDTH + x;
        R[index] = 0;
        G[index] = 255;
        B[index] = 0;
    }
    return 1;
} 

/*int draw_cross(int character, const char* output_file, unsigned char* R, unsigned char* G, unsigned char* B) {
    FILE* f = fopen(output_file, "r");
    if (f == NULL) {
        fprintf(stderr, "Failed to open output file: %s\n", output_file);
        return 0;
    indicator(character, output_file, R, G, B);
    
    }
    int cx=0;
    int cy=0;   
    fscanf(f, "%d", &cx);
    fscanf(f, "%d", &cy);
    printf("%d  %d", cx, cy);

    int size = 10;
    int x_start = cx - size/2;
    int x_end = cx + size/2;
    int y_start = cy - size/2;
    int y_end = cy + size/2;
    for (int y = y_start; y <= y_end; y++) {
        int index = y * IMAGE_WIDTH + cx;
        R[index] = 255;
        G[index] = 0;
        B[index] = 0;
    }
    for (int x = x_start; x <= x_end; x++) {
        int index = cy * IMAGE_WIDTH + x;
        R[index] = 0;
        G[index] = 255;
        B[index] = 0;
    }
    fclose(f);
    return 1;
}*/


int caracter(const char *filename,const char* output_file,const char* ffilename, unsigned char* R, unsigned char* G, unsigned char* B) {
    char choice;
    printf("Choose a character Yoda(Y), DMaul(D), Mando(M): ");
    scanf(" %c", &choice);

    switch(choice) {
        case 'Y':
            read_rgb_image("starwars.rgb", R, G, B);
            hue_image("Hue",R,G,B);
            indicator(1,output_file,R,G,B);
            draw_cross(1,output_file,R,G,B);
            write_rgb_image(ffilename,R,G,B);
            break;
        case 'D':
            read_rgb_image("starwars.rgb", R, G, B);
            hue_image("Hue",R,G,B);
            indicator(2,output_file,R,G,B);
            draw_cross(2,output_file,R,G,B);
            write_rgb_image(ffilename,R,G,B);
            break;
        case 'M':
            read_rgb_image("starwars.rgb", R, G, B);
            hue_image("Hue",R,G,B);
            indicator(2,output_file,R,G,B);
            draw_cross(2,output_file,R,G,B);
            write_rgb_image(ffilename,R,G,B);
            break;
        default:
            printf("Invalid choice.\n");
            return 0;
    }

    return 1;
}
int main(int argc, char const *argv[])
{
    unsigned char *R = (unsigned char*)malloc(sizeof(unsigned char) * IMAGE_WIDTH * IMAGE_HEIGHT);
    unsigned char *G = (unsigned char*)malloc(sizeof(unsigned char) * IMAGE_WIDTH * IMAGE_HEIGHT);
    unsigned char *B = (unsigned char*)malloc(sizeof(unsigned char) * IMAGE_WIDTH * IMAGE_HEIGHT);
    caracter("Starwars.rgb","Hue","Final.rgb",R,G,B);
     /*read_rgb_image("starwars.rgb", R, G, B);
     const char* output_file = "output";
     const char* ffilename = "out";
            hue_image("Hue",R,G,B);
            indicator(2,output_file,R,G,B);
            draw_cross(2,output_file,R,G,B);
            /*write_rgb_image(ffilename,R,G,B);*/
            
    return 0;
}
