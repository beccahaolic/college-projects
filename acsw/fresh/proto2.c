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

    unsigned char temp[altura*largura*3]; // Read the image into memory temporary variable
    fread(temp, sizeof(unsigned char), altura*largura*3, fp);
    fclose(fp);
    for (int i = 0; i < altura*largura; ++i) { // Copy the image data to the output matrices
        R[i] = temp[3*i];
        G[i] = temp[3*i+1];
        B[i] = temp[3*i+2];
    }
}

int hue (int r, int g, int b){
    int HUE; float X;

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

    return HUE;
}

int indicator(int character, unsigned char R, unsigned char G, unsigned char B) { // Check if each pixel belongs to the specified character
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
        for (int x = 0; x < largura; x++) {// Check if pixel (x,y) belongs to character p
            int r = R[index];
            int g = G[index];
            int b = B[index];

            int belongs_to_p = indicator(character, r, g, b);
            if (belongs_to_p == 1) { //se pertence ao personagem, incrementa o numero de x, y e N;
                N++;
                x_sum += x;
                y_sum += y;
            }
            index++;
        }
    }
    if (N > 0) { //calcula centro de massa
        cx = x_sum / N;
        cy = y_sum / N;
        coordinates[0] = cx;
        coordinates[1] = cy;
    } 
    else {// No pixels belonging to character p were found, return an invalid value
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
    int size = 5;

    int x_start = cx - size;
    int x_end = cx + size;
    int y_start = cy - size;
    int y_end = cy + size;

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
    
    for (int i = 0; i < altura*largura; ++i) {    // Write the image data to the file
        fputc(R[i], fp);
        fputc(G[i], fp);
        fputc(B[i], fp);
    }
    fclose(fp);
 }

 int caracter(const char* filename, const char* ffilename, unsigned char* R, unsigned char* G, unsigned char* B, int coordinates[2]) {
    char choice[15];
    printf("Choose a character Yoda, Darth Maul, Mandalorian : ");
    scanf("15%c", &choice);

    switch(choice) {
        case "Yoda":
            read_rgb_image("starwars.rgb", R, G, B);
            draw_cross(1, R, G, B, coordinates); 
            write_rgb_image(ffilename, R, G, B);
            break;
        case "Darth Maul":
            read_rgb_image("starwars.rgb", R, G, B);
            draw_cross(2, R, G, B, coordinates); // pass coordinates to draw_cross
            write_rgb_image(ffilename, R, G, B);
            break;
        case "Mandalorian":
            read_rgb_image("starwars.rgb", R, G, B);
            draw_cross(3, R, G, B, coordinates); // pass coordinates to draw_cross
            write_rgb_image(ffilename, R, G, B);
            break;
        default:
            printf("Invalid choice.\n");
            return 0;
    }
    return 1;
}

int main(int argc, char const *argv[]) {
    
    unsigned char R[altura*largura];
    unsigned char G[altura*largura];
    unsigned char B[altura*largura];

    int coordenadas[2];
    int x = caracter("starwars.rgb","quaqua.rgb",R,G,B,coordenadas);v 
}
