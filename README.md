## Aplicação Java para detecção e reconhecimento de faces

Este é um aplicativo de demonstração desenvolvido em Java 17 com maven e apoio da biblioteca JavaCV.

## Requisitos

Necessário ter um JDK 17 ou superior.

IDE de sua preferência ( Foi utilizado Netbeans pela facilidade de utilização da criação dos recursos gráficos).

Arquivos `registroUsuarios.json` e `haarcascade_frontalface_alt.xml`  dentro do diretório de resources da aplicação.
Esse arquivos serão utilizados para armazenamento dos usuários cadastrados e para o reconhecimento de faces. O Projeto já contém uma cópia dos arquivos.

## Execução
Ao executar será apresentado o formulário principal onde você poderá estar
realizando o cadastro de faces, e também o reconhecimento delas.
<div align="center">
  <img src="https://user-images.githubusercontent.com/30123041/197622345-22888b9e-481d-4c24-87cf-1c793ebf0904.png"/>
</div>  


Para o registro é necessário inserir um nome e após a face ser reconhecida realizar a captura de 25 fotos, basta manter pressionado o botão capturar.
<div align="center">
  <img src="https://user-images.githubusercontent.com/30123041/197623837-b3eea08a-996c-49e3-a7c2-a4d16f80631e.png"/>
</div>  

As imagens salvas serão mantidas no diretório do projeto ex... `JavaFaceRecognition\src\main\resources\imagens`

E será adicionado o usuário cadastrado no arquivo `registroUsuarios.json` para posterior reconhecimento.
Após um usuário ser registrado será feito o treinamento e será gerado junto com as imagens
um arquivo `classifierLBPH.yml`.

O reconhecimento será gerado apartir da imagens e o arquivo de classificação.
<div align="center">
  <img src="https://user-images.githubusercontent.com/30123041/197624225-a85056e3-c0b5-462a-9bf8-ef9d89ba1f89.png"/>
</div>  
