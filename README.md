## ⚙️ Como Executar o Projeto

### 1. Configurar a Base de Dados
1. Abra o seu MySQL Workbench (ou phpMyAdmin).
2. Execute o script SQL que se encontra na pasta `sql/parque_db.sql`. 
   *(Isto criará a base de dados `parque_db` e as 30 vagas iniciais).*

### 2. Configurar a Conexão no Java
1. Importe o projeto para o Eclipse.
2. Abra a classe `ConexaoBD.java` (localizada em `src/persistencia`).
3. Altere as variáveis `UTILIZADOR` e `PASSWORD` para as suas credenciais locais do MySQL.

### 3. Executar
1. Execute a classe `Principal.java` (localizada em `src`, fora de qualquer pacote).