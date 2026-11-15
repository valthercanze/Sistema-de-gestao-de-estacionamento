-- Criação da base de dados
DROP DATABASE IF EXISTS parque_db;
CREATE DATABASE parque_db;
USE parque_db;

-- Criação da tabela de Vagas
CREATE TABLE vaga (
    numero   INT PRIMARY KEY,
    tipo     VARCHAR(20) NOT NULL,
    ocupada  BOOLEAN NOT NULL DEFAULT FALSE
);

-- Criação da tabela de Bilhetes (Histórico)
CREATE TABLE ticket (
    id            INT PRIMARY KEY AUTO_INCREMENT,
    matricula     VARCHAR(20) NOT NULL,
    tipo_veiculo  VARCHAR(20) NOT NULL,
    marca         VARCHAR(50) NOT NULL,
    modelo        VARCHAR(50) NOT NULL,
    numero_vaga   INT NOT NULL,
    hora_entrada  DATETIME NOT NULL,
    hora_saida    DATETIME NULL,
    valor_pago    DECIMAL(10,2) NULL,
    FOREIGN KEY (numero_vaga) REFERENCES vaga(numero)
);

-- Inserção das 30 vagas (15 Carros, 10 Motas, 5 Camiões)
INSERT INTO vaga (numero, tipo, ocupada) VALUES
(1,'CARRO',FALSE),(2,'CARRO',FALSE),(3,'CARRO',FALSE),(4,'CARRO',FALSE),(5,'CARRO',FALSE),
(6,'CARRO',FALSE),(7,'CARRO',FALSE),(8,'CARRO',FALSE),(9,'CARRO',FALSE),(10,'CARRO',FALSE),
(11,'CARRO',FALSE),(12,'CARRO',FALSE),(13,'CARRO',FALSE),(14,'CARRO',FALSE),(15,'CARRO',FALSE),
(16,'MOTA',FALSE),(17,'MOTA',FALSE),(18,'MOTA',FALSE),(19,'MOTA',FALSE),(20,'MOTA',FALSE),
(21,'MOTA',FALSE),(22,'MOTA',FALSE),(23,'MOTA',FALSE),(24,'MOTA',FALSE),(25,'MOTA',FALSE),
(26,'CAMIAO',FALSE),(27,'CAMIAO',FALSE),(28,'CAMIAO',FALSE),(29,'CAMIAO',FALSE),(30,'CAMIAO',FALSE);