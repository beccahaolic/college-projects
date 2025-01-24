package com.iotproject.payload.response;

/**
* Classe que representa uma resposta contendo uma mensagem.
* Usada para enviar mensagens simples como respostas de API.
*/
public class MessageResponse {
  private String message; // Mensagem da resposta

  /**
  * Construtor com argumento.
  * 
  * @param message Mensagem da resposta.
  */
  public MessageResponse(String message) {
    this.message = message;
  }

  // Getters e Setters para os atributos da classe
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}