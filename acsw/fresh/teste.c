#include <stdio.h>
#include <stdlib.h>

#define altura  180
#define largura 320

/*int *read_rgb_image (char filename, unsigned char array[altura*largura]){
    unsigned char r,g,b;
    FILE* imagem = fopen("filename",rb);

    if (fp == NULL) {
        printf("Error: Unable to open file\n");
        exit(1);
    }

    unsigned char temp[altura*largura*3];
    fread(temp, sizeof(unsigned char), altura*largura*3, fp);
    fclose(fp);

    if( strcmp(cor,'r') == 0){
        for (i=0; i = altura*largura; i++){
            array[i*3]
        }
    }



return array;
}*/

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
        //printf("pixel: %d : %hhu %hhu %hhu\n",i,R[i], G[i], B[i]);
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
    
    else
      printf("erroe hue\n");

}

int main(){

    unsigned char R[altura*largura];
    unsigned char G[altura*largura];
    unsigned char B[altura*largura];
    read_rgb_image("starwars.rgb",R, G, B);

    int huue = hue(R[0],G[0],B[0]);
    printf("%d",huue);
 

}