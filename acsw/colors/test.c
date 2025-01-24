#include <stdio.h>
#include <stdlib.h>

// Define a struct for RGB pixel
typedef struct {
    unsigned char r, g, b;
} pixel;

// Define a function to read an RGB file and convert it to a 2D array
pixel** read_rgb_file(const char* filename, int* width, int* height) {
    // Open the file for reading
    FILE* fp = fopen(filename, "rb");
    if (fp == NULL) {
        printf("Failed to open file: %s\n", filename);
        return NULL;
    }

    // Read the width and height from the file
    fscanf(fp, "%d %d\n", width, height);

    // Allocate memory for the pixel array
    pixel** image = (pixel**) malloc(*height * sizeof(pixel*));
    for (int i = 0; i < *height; i++) {
        image[i] = (pixel*) malloc(*width * sizeof(pixel));
    }

    // Read the pixel values from the file
    for (int i = 0; i < *height; i++) {
        for (int j = 0; j < *width; j++) {
            unsigned char r, g, b;
            if (fscanf(fp, "%hhu %hhu %hhu", &r, &g, &b) != 3) {
                printf("Error reading pixel (%d, %d) from file: %s\n", j, i, filename);
                return NULL;
            }
            image[i][j].r = r;
            image[i][j].g = g;
            image[i][j].b = b;
            printf("pixel(%p, %p): %d %d %d",width,height,r,g,b);
        }
    }

    // Close the file and return the pixel array
    fclose(fp);
    return image;
}

int main(){
    int width, height;
    pixel** image =read_rgb_file("starwars.rgb",&width, &height);
    return 0;
}