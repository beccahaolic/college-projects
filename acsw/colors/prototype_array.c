#include <stdio.h>
#include <stdlib.h>

#define LARG  320
#define ALT 180

typedef struct RGB{
    unsigned char red;
    unsigned char green;
    unsigned char blue;
    //elementType *Array;
}pixel;

int* read_rgb(const char* filename, array[i][j]){

    int r, g,b;
    int red, green,blue;

    FILE* imagem = fopen(filename, "rb"); //abrir ficheiro

    if(!imagem){
        printf("can't open file\n");
        return NULL;
    }

    i = ALT;
    j = LARG;





}

int main(){
    



}