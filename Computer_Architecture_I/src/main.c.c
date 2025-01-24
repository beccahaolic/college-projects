#include <stdio.h>
#include <stdlib.h>
#define altura  180
#define largura 320

void read_rgb_image(const char *filename, unsigned char *R, unsigned char *G, unsigned char *B) {
    FILE *fp = fopen(filename, "rb");  // open file in binary mode
    if (fp == NULL) {
        printf("Error: Unable to open file\n");
        exit(1);
    }
    // Read the image into memory
    unsigned char temp[altura*largura*3];
    fread(temp, sizeof(unsigned char), altura*largura*3, fp);
    fclose(fp);
    // Copy the image data to the output matrices
    for (int i = 0; i < altura*largura; ++i) {
        R[i] = temp[3*i];
        G[i] = temp[3*i+1];
        B[i] = temp[3*i+2];
    }
}

int hue (int r, int g, int b){
    int HUE;
    float X;   
    if (r > g && g >= b) {
        X = (g - b);
        X = X / (r - b);
        HUE = 60 * X;
        return HUE;
    } 
    else if (g >= r && r > b) {
        X = (g - r);
        X = X / (g - b);
        HUE = 60 * X + 60;
        return HUE;
    } 
    else if (g > b && b >= r) {
        X = (b - r);
        X = X / (g - r);
        HUE = 60 * X + 120;
        return HUE;
    } 
    else if (b >= g && g > r) {
        X = (b - g);
        X = X / (b - r);
        HUE = 60 * X + 240;
        return HUE;
    } 
    else if (b > r && r >= g) {
    X = (r - g);
    X = X / (b - g);
    HUE = 60 * X + 300;
    return HUE;
    } 
    else if (r >= b && b > g) {
        X = (r - b);
        X = X / (r - g);
        HUE = 60 * X + 360;
    return HUE;
    }
}

int indicator(int character, unsigned char R, unsigned char G, unsigned char B) {
    // Check if each pixel belongs to the specified character
    int huue = hue(R, G, B);

    if (character == 1 && huue >= 40 && huue <= 80)// pertence ou yoda
    return 1;

    else if (character == 2 && huue >= 1 && huue <= 15)// pertence ao darth maul
    return 1;

    else if (character == 3 && (huue >= 160 && huue <= 180))// pertence ao mandalorian
    return 1;

    else
    return 0;
}

int *center_of_mass(int character, unsigned char *R,  unsigned char *G, unsigned char *B, int coordinates[2]) {
    int N = 0; //->total of pixels in the character
    int x_sum = 0; //-> all x's in the character
    int y_sum = 0; //-> all y's in the characther
    int cx, cy; //->coordinates of CM
    int index = 0;

    for (int y = 0; y < altura; y++) {
        for (int x = 0; x < largura; x++) {
            // Check if pixel (x,y) belongs to character p
            int r = R[index];
            int g = G[index];
            int b = B[index];

            int belongs_to_p = indicator(character, r, g, b);

            if (belongs_to_p == 1 ) {
                // Increment counters
                N++;
                x_sum += x;
                y_sum += y;
            }
            index++;
        }
    }
    // Calculate center of mass
    if (N > 0) {
        cx = x_sum / N;
        cy = y_sum / N;
        coordinates[0] = cx;
        coordinates[1] = cy;
        printf("%d\n", N);
    } 
    else {
        // No pixels belonging to character p were found, return an invalid value
        cx = 0;
        cy = 0;
        coordinates[0] = cx;
        coordinates[1] = cy;
    }
    return coordinates;
}

int draw_cross(int character, unsigned char* R, unsigned char* G, unsigned char* B,int coordinates[2]) {
    center_of_mass(character,R,G,B,coordinates);
    int cx = coordinates[0];
    int cy = coordinates[1];
    int size = 10;
    int x_start = cx - size/2;
    int x_end = cx + size/2;
    int y_start = cy - size/2;
    int y_end = cy + size/2;
    for (int y = y_start; y <= y_end; y++) {
        int index = y * largura + cx;
        R[index] = 255;
        G[index] = 0;
        B[index] = 0;
    }
    for (int x = x_start; x <= x_end; x++) {
        int index = cy * largura + x;
        R[index] = 0;
        G[index] = 255;
        B[index] = 0;
    }
    return 1;
} 

void write_rgb_image(const char *ffilename,unsigned char *R, unsigned char *G, unsigned char *B) {
    FILE *fp = fopen(ffilename, "wb");  // open file in binary mode
    if (fp == NULL) {
        printf("Error: Unable to open file\n");
        exit(1);
    }
    // Write the image data to the file
    for (int i = 0; i < altura*largura; ++i) {
        fputc(R[i], fp);
        fputc(G[i], fp);
        fputc(B[i], fp);
    }
 fclose(fp);
 }

int caracter(const char* filename, const char* ffilename, unsigned char* R, unsigned char* G, unsigned char* B, int coordinates[2]) {
    char choice;
    printf("Choose a character Yoda(Y), DMaul(D), Mando(M): ");
    scanf(" %c", &choice);
    int coordenadas[2]; // declare coordinates variable here

    switch(choice) {
        case 'Y':
            read_rgb_image("starwars.rgb", R, G, B);
            draw_cross(1, R, G, B, coordenadas); // pass coordinates to draw_cross
            write_rgb_image(ffilename, R, G, B);
            break;
        case 'D':
            read_rgb_image("starwars.rgb", R, G, B);
            draw_cross(2, R, G, B, coordenadas); // pass coordinates to draw_cross
            write_rgb_image(ffilename, R, G, B);
            break;
        case 'M':
            read_rgb_image("starwars.rgb", R, G, B);
            draw_cross(3, R, G, B, coordenadas); // pass coordinates to draw_cross
            write_rgb_image(ffilename, R, G, B);
            break; 
        default:
            printf("Invalid choice.\n");
            return 0;
    }
    // copy the coordinates from local variable coordenadas to the output parameter coordinates
    coordinates[0] = coordenadas[0];
    coordinates[1] = coordenadas[1];
    return 1;
}

int main(int argc, char const *argv[]) {
    unsigned char R[altura*largura];
    unsigned char G[altura*largura];
    unsigned char B[altura*largura];
    int coordinates[2];
   caracter("Starwars.rgb","Final.rgb",R,G,B,coordinates);
}