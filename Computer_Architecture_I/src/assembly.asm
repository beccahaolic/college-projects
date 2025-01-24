.data
input: .asciz "starwars.rgb"  # Nome do arquivo de entrada
output: .asciz "Newstarwars.rgb"  # Nome do arquivo de entrada
error: .asciz "Erro na leitura do arquivo"  # Mensagem de erro
personagem: .asciz "Escolha um personagem: Yoda, Darth Maul, Mandalorian"
erro_personagem: .asciz "Personagem inválido, por favor escolha um desses: Yoda, Darth Maul, Mandalorian. Não se esqueça de escrever exatamanete como é mostrado!"
yoda_p: .asciz "Yoda"
darthmaul_p: .asciz "Darth Maul"
mandalorian_p: .asciz "Mandalorian" 
escolha: .asciz "%s"
Image:.space  172800    # array para armazenar os dados da imagem RGB (largura * altura * 3 bytes)
Ponto: .word 50

.text
.globl main 

###################################################################################################################################################
######################################################
# Funcao: strcmp
# Descricao: Verifica se duas strings são iguais ou diferentes
# Argumentos:
# a0 - string 1 -> vem do syscall 54
# a1 - string 2
# Retorna:
# a0 - 1 se são iguais ou 0 se são diferentes (salvo em s0)
######################################################
strcmp:
	# remove /n from string
	li t1, 10
	loop:
	lbu t0, 0(a0)         # Load the character from memory
	beqz t0, remove
	addi a0, a0, 1
	j loop
        
remove:
	addi a0, a0, -1
	sb zero, 0(a0)        # Replace the /n with null character
	bne t0, t1, completed # 10 é ascii para /n
        
completed:
	li a0, 0
	la a0, escolha
    
compare_loop:
	lbu t1, 0(a0) 
	lbu t2, 0(a1)
	beqz t1, equal # se chegar no NULL character eles são iguais 
	bne t1, t2, end_loop # se for diferente, sai do loop
	
	addi a0, a0, 1 # avança byte
	addi a1, a1, 1 # avança byte
	j compare_loop

equal:
	li s0, 1
end_loop:
	ret
    
###################################################################################################################################################
######################################################
# Funcao: ask_character
# Descricao: Verifica se personagem inserido é válido e atribui um valor 1, 2, 3
# Argumentos:
# a0 - string 1 -> vem de syscall 54 (personagem escolhido pelo user)
# Retorna:
# a0 - 1 se for yoda, 2 se for darth maul, 3 se for mandalorian (salvo em s0)
######################################################
ask_character: 
	addi sp, sp, -4
	sw ra, 0(sp)

	loopastico:	
	li t3, 1
	
	la a1, yoda_p
	jal strcmp
	bne s0, t3, darth
	li s0, 1
	j out
	
	darth:
	la a1, darthmaul_p
	jal strcmp
	bne s0, t3, mandal
	li s0, 2
	j out
	
	mandal:
	la a1, mandalorian_p
	jal strcmp
	bne s0, t3, invalid
	li s0, 3
	j out
	
	invalid:
	la a0, erro_personagem
	la a1, escolha
	li a2, 15
	li a7, 54
	ecall
	la a0, escolha
	j loopastico
	
	out:
	lw ra, 0(sp)
	addi sp, sp, 4
		
	mv a0, s0
	ret
#######################################################################################################################

######################################################
# Funcao: read_rgb_image
# Descricao: Converte uma imagem no formato .rgb em um array de valores RGB
# Argumentos:
# a0 - Ficheiro a ser lido
# a1 - array onde será escrito os valores RGB
# Retorna:
# Void -> escreve valores RGB no array Image
######################################################
read_rgb_image: 
    addi sp, sp, -8
    sb s0, 0(sp)
    sb s1, 4(sp)

    mv s0, a1  # Endereço do array onde a imagem será armazenada
    li a7, 1024  # Chamada de sistema para abrir arquivo
    li a1, 0     # Abre o arquivo para leitura (flags são 0: leitura, 1: escrita)
    ecall        # Abre o arquivo (o descritor do arquivo é retornado em a0)
    mv s1, a0    # Salva o descritor do arquivo

    beqz a0, read_rgb_image_error  # Verifica se o arquivo foi aberto com sucesso

    # Lê os dados da imagem do arquivo e armazena no array
    li a7, 63    # Chamada de sistema para leitura
    mv a0, s1    # Carrega o descritor do arquivo em a0
    mv a1, s0    # Carrega o endereço do array de imagem em a1
    li a2, 172800  # Lê até 172800 bytes (tamanho do arquivo starwars.rgb)
    ecall        # Lê os dados da imagem do arquivo

read_rgb_image_done:
    li a7, 57    # Chamada de sistema para fechar o arquivo
    mv a0, s1    # Move o descritor do arquivo para a0 para fechar o arquivo
    ecall
    li a0, 0
    li a1, 0
    li a2, 0
    li a3, 0
    addi sp, sp, 8
    lb s0, 0(sp)
    lb s1, 4(sp)
    ret

read_rgb_image_error: # Print da mensagem de erro
    la a0, error
    li a7, 4
    ecall
    j read_rgb_image_done  # Salto para o fim da função
      
 ######################################################################################################################################################
######################################################
# Funcao: hue
# Descricao: Calcula valor Hue em º a partir de componentes RGB
# Argumentos:
# a0 - array Image
# a1 - nº do pixel atual
# Retorna:
# a0 - Hue (salvo em s0)
######################################################
 hue: 
	add a4, a0, a1 #calcula pixel/endereço 
	lbu t0, 0(a4)
	lbu t1, 1(a4) 
	lbu t2, 2(a4) 

 #################### laranja #######################
	ble t0, t1, Verde_amarelado #r<g
	blt t1, t2, Verde_amarelado #g < b
		  
	sub t3, t1, t2  # g - b
	sub t4, t0, t2  # r - b
	addi t5, zero, 60 
	mul t3, t3, t5 #*60
	div t3, t3, t4
	j final
	   
Verde_amarelado:    
	blt t1, t0, Verde_primavera
	ble t0, t2, Verde_primavera
			 
	sub t3, t0, t2  # r - b
	sub t4, t1, t2  # g - b
	addi t5, zero, 60  # *60
			 
	mul t3, t3, t5
	div t3, t3, t4
	addi t6, zero, 120
	sub t3, t6, t3
	j final
	    
Verde_primavera: 
	ble  t1, t2, Azure
	blt t2, t0, Azure
				  
	sub t3, t2, t0  # b - r
	sub t4, t1, t0  # g - r
	addi t5, zero, 60  # *60
				  
	mul t3, t3, t5
	div t3, t3, t4
	addi t3, t3, 120  
	j final
		
Azure: 
	ble t1, t0, violeta
	blt  t2, t1, violeta
				  
	sub t3, t1, t0  # g - r
	sub t4, t2, t0  # b - r
	addi t5, zero, 60  # *60
				  
	mul t3, t3, t5
	div t3, t3, t4
	addi t6, zero, 240
	sub t3, t6, t3  
	j final
	   
violeta: 
	ble  t2, t0, rosa
	blt t0, t1, rosa
				  
	sub t3, t0, t1  # r - g
	sub t4, t2, t1  # b - g
	addi t5, zero, 60  # *60
				  
	mul t3, t3, t5
	div t3, t3, t4
	addi t3, t3, 240   
	j final
				   
rosa:
	blt t0, t2, same
	ble t2, t1, same
			
	sub t3, t2, t1  # b - g
	sub t4, t0, t1  # r - g
	addi t5, zero, 60  # *60
			
	mul t3, t3, t5
	div t3, t3, t4
	addi t5, zero, 360
	sub t3, t5, t3
	j final
			  
same: ########## caso r, g, b, sejam iguais ##################
	li t3, 0 ## hue = 0
			  
final:
	mv s0, t3
	ret
  
  ########################################################################################################################################################
######################################################
# Funcao: indicator
# Descricao: Indica se um pixel pertence ou não a um personagem
# Argumentos:
# a0 - array Image
# a1 - pixel atual
# a2 - personagem (1, 2, 3)
# Retorna:
# a0 - 0 se não pertence, 1 se pertence (valor salvo em s1)
######################################################
indicator:
	addi sp, sp, -4
	sw ra, 0(sp)
	
	  jal hue

	  li t1, 1
	  li t2, 2
	  li t3, 3
	  li t4, 0
	  
	  beq  a2, t1, yoda      # se character == 1, pula para a label yoda
	  beq a2, t2, darth_maul # se character == 2, pula para a label darth_maul
	  beq a2, t3, mandalorian # se character == 3, pula para a label mandalorian

yoda:  
	li t4, 40
	blt s0, t4, n_pertence # s0 é o valor que vem da função hue
	li t4, 80
	bgt s0, t4, n_pertence 
	li t4, 1 ## retorna 1
	j saida
			
darth_maul: 
	li t4,1
	blt s0, t4, n_pertence
	li t4, 15
	bgt s0, t4, n_pertence
	li t4,1 ##retorna 1
	j saida

mandalorian:
	li t4, 160
	blt s0, t4, n_pertence
	li t4, 180
	bgt s0, t4, n_pertence
	li t4, 1 ##retorna 1
	j saida
			
n_pertence:
	li t4, 0  ##retorna 0
		
saida:
	lw ra, 0(sp)
	addi sp, sp, 4
	mv s1, t4
	ret

##################################################################################################################################################
######################################################
# Funcao: centro_de_massa
# Descricao: Calcula o centro de massa de um personagem
# Argumentos:
# a0 - Array Image
# a1 - Pixel atual
# a2 - personagem (1, 2, 3)
# Retorna:
# a0 - array Ponto (com o ponto do centro de massa)
######################################################
centro_de_massa:
	addi sp, sp, -4
	sw ra, 0(sp)
	
	li s2, 0 #controle x
	li s3, 0 #controle y
	li s4, 0 # todas coordenadas x do personagem
	li s5, 0 #todas as coordenadas y do personagem
	
	li s6, 0 #conta quantos pixels pertencem ao personagem
	li s8, 180 # altura
	li s7, 320 # largura 
	
loop_altura:
	bge s3, s8, fim_altura
	li s2, 0 #-> reseta x a cada nova linha
	
	loop_largura:
	bge  s2, s7, fim_largura
		
	jal indicator
	
	beqz s1, dont_belong
	addi s6, s6, 1  #aumenta nº de pixels q pertence ao personagem
	add s4, s4, s2 #aumenta soma de todos os x
	add s5, s5, s3 #aumenta soma de todos os y
		
	dont_belong:
	addi a1, a1, 3 # ajusta endereço -> o resto do calculo de como ajusta o endereço tá na hue
	addi s2, s2, 1 #aumenta contador x
	j loop_largura
		
	fim_largura:
	addi s3, s3, 1 #aumenta contador y
	j loop_altura
		
fim_altura:
	lw ra, 0(sp)
	addi sp, sp, 4
	div s4, s4, s6 # faz a média/centro de massa do x
	div s5, s5, s6 #mesma coisa só que com o y
		
	la a0, Ponto #coloca o resultado num array chamado Ponto
	sw s4, 0(a0) #usamos sw pois um dos pontos tem valor > 255
	sw s5, 4(a0) 
	ret
		
###########################################################################################################
######################################################
# Funcao: draw_cross
# Descricao: Modifica a imagem e desenha uma cruz no centro de imagem do personagem
# Argumentos:
# a0 - Array Image
# a1 - Array Ponto
# Retorna:
# a0 - Array Image modificado
######################################################
draw_cross:
	lw t0, 0(a1)
	lw t1, 4(a1)
	li s0, 320    # Largura da imagem
	
	li t2, 5  # Cálculo de x_start = cx - 5
	sub t2, t0, t2
	
	li t3, 5   # Cálculo de x_end = cx + 5
	add t3, t0, t3
	  
	li t4, 5   # Cálculo de y_start = cy - 5
	sub t4, t1, t4
	  
	li t5, 5   # Cálculo de Y_end = cy + 5
	add t5, t1, t5
	  
loop_y:   # Loop para o eixo y
	bgt t4, t5, end_loop_y
	    # Cálculo do índice = cx + y * largura
	mul s1, t4, s0   # y * largura
	add s1, t0, s1   # cx + y * largura
	slli s2, s1, 1   # (cx + y * largura) * 2
	    
	add s1, s1, s2   # (cx + y * largura) * 3
	add s1, a0, s1    # endereco base para
	    
	li s2, 255
	sb s2, 0(s1)
	    
	addi s1, s1, 1   # Incremento de 1 byte para accessar verde
	sb zero, 0(s1)
	addi s1, s1, 1   # Incremento de 1 byte para acessar o canal azul
	    
	sb zero, 0(s1)
	addi s1, s1, 3   # Mover para a próxima posição do pixel
	
	addi t4, t4, 1   # Incremento de y em 1
	j loop_y
    
end_loop_y:
	li s1, 0
	li s2, 0

loop_x:   # Loop para o eixo x
	bgt t2, t3, end_draw_cross
	    
	    # Cálculo do índice = x + cy * largura
	mul s1, t1, s0   # cy * largura
	add s1, t2, s1   # x + cy * largura
	slli s2, s1, 1   # (cx + y * largura) * 2
	add s1, s1, s2   # (cx + y * largura) * 3
	    
	li s2, 255     
	add s1, a0, s1 # endereco base para
	    
	sb zero, 0(s1)
	addi s1, s1, 1   # Incremento de 1 byte para o canal verde
	    
	 sb s2, 0(s1)
	 addi s1, s1, 1   # Incremento de 1 byte para o canal azul
	    
	sb zero, 0(s1)
	addi t2, t2, 1   # Incremento de x em 1
	
	j loop_x
    
end_draw_cross:
	ret

########################################################################################################
######################################################
# Funcao: write_rgb_image
# Descricao: Lê um array de valores RGB  e converte para um ficheiro de formato .rgb
# Argumentos:
# a0 - nome do novo ficheiro .rgb
# a1 - array Image (que contém valor RGB e cruz)
# Retorna:
# Void -> escreve novo ficheiro no formato .rgb 
######################################################
write_rgb_image:
	addi sp, sp, -8
	sb s0, 0(sp)
	sb s1, 4(sp)
	    
	mv s0,a1
	  # Open the file in binary mode for writing
	li a7, 1024        # System call for opening a file
	li a1, 1           # Open the file for writing
	ecall              # Open the file (the file descriptor is returned in a0)
	mv s1, a0          # Save the file descriptor
	
	beqz a0, write_rgb_image_error   # Check if the file was opened successfully
	
	li a7, 64          # System call for writing
	mv a0, s1          # Load the file descriptor in a0
	mv a1,s0
	li a2, 172800      # Write up to 172800 bytes (size of the RGB array)
	ecall              # Write the RGB data to the file
	
	li a7, 57          # System call for closing a file
	mv a0, s0          # Move the file descriptor to a0 to close the file
	ecall              # Close the file
	ret

write_rgb_image_error:   # Print error message
	la a0, error
	li a7, 4
	ecall
	addi sp, sp, 8
	lb s0, 0(sp)
	lb s1, 4(sp)
	ret
  ############################################################################################################################################################
			
main:
	la a0, input #nome de ficheiro a ser lido
	la a1, Image  # Endereço onde a imagem será armazenada 
	jal read_rgb_image
	
	la a0, personagem
	la a1, escolha
	li a2, 15
	li a7, 54
	ecall
	
	la a0, escolha
	jal ask_character
	
	mv a2, a0 #-> a2 tem nº do personagem escolhido
	li s0, 0
	
	la a0, Image
	li a1, 0 #-> nº dppixel atual
	
	jal centro_de_massa
	
	la a0, Image
	la a1, Ponto
	jal draw_cross
	
	 la a0, output #nome do novo ficheiro rgb
	 la a1, Image  # Endereço onde a imagem será armazenada 
	jal write_rgb_image
	
	li a7, 10  # Exit the program
	ecall


	
	
