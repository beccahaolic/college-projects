int* read_rgb_image(const char* filename) {
    FILE* image = fopen(filename, "rb");
    if (image == NULL) {
        fprintf(stderr, "Error: could not open file %s\n", filename);
        return NULL;
    }


    *width = 320;
    *height = 180;

    int size = *width * *height * 3;

    unsigned char* coisas = (unsigned char*)malloc(size);
    if (coisas == NULL) {
        fclose(image);
        fprintf(stderr, "Error: could not allocate memory for image coisas\n");
        return NULL;
    }

    fread(coisas, sizeof(unsigned char), size, image);

    // Close the file
    fclose(image);

    
    int* array = (int*)malloc(*width * *height * sizeof(int));
    if (array == NULL) {
        free(coisas);
        fprintf(stderr, "Error: could not allocate memory for array\n");
        return NULL;
    }

    for (int i = 0; i < *width * *height; i++) {
        array[i] = ((int)coisas[i * 3] << 16) | ((int)coisas[i * 3 + 1] << 8) | (int)coisas[i * 3 + 2];
    }

    free(coisas);

    // Return the array
    return array;
}



int hue_image(unsigned char (*image_data)[IMAGE_WIDTH][3])
{
    int R, G, B;
    for (int i = 0; i < IMAGE_HEIGHT; ++i) {
        for (int j = 0; j < IMAGE_WIDTH; ++j) {
            // Get the RGB values
            R = (int)(image_data[0][i][j] * 255) / 255;
            G = (int)(image_data[1][i][j] * 255) / 255;
            B = (int)(image_data[2][i][j] * 255) / 255;

            // Convert to HUE
            if (R > G && G >= B)
            {
               return 60 * ((G - B) / (R - B));
            }
            else if (G >= R && R > B)
            {
                return (120 - 60) * ((R - B) / (G - B));
            }
            else if (G > B && B >= R)
            {
                return (120 + 60) * ((B - R) / (G - R));
            }
            else if (B >= G && G > R)
            {
                return (240 - 60) * ((G - R) / (B - R));
            }
            else if (B > R && R >= G)
            {
                return (240 + 60) * ((G - R) / (B - R));
            }
            else if (R >= B && B > G)
            {
                return (360 - 60) * ((B - G) / (R - G));
            }
        }
    }
}