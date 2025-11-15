create database quase_tudo_gostoso_java;
use quase_tudo_gostoso_java;

CREATE TABLE usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    cep INT NOT NULL,
    genero INT,
    senha VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    inscrito VARCHAR(50),
    uuid VARCHAR(100),
    ativo TINYINT(1) NOT NULL
);

CREATE TABLE receita (
    idReceita INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    imagem VARCHAR(255),
    idUsuario INT NOT NULL,
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
);

CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoria VARCHAR(150) NOT NULL,
    ativo TINYINT(1) NOT NULL
);

CREATE TABLE receita_categoria (
    idReceita INT NOT NULL,
    idCategoria INT NOT NULL,
    PRIMARY KEY (idReceita, idCategoria),
    FOREIGN KEY (idReceita) REFERENCES receita(idReceita),
    FOREIGN KEY (idCategoria) REFERENCES categoria(id)
);

CREATE TABLE comentario (
    idComentario INT AUTO_INCREMENT PRIMARY KEY,
    idReceita INT NOT NULL,
    idUsuario INT NOT NULL,
    comentario TEXT NOT NULL,
    nota INT,
    dataComentario VARCHAR(50),
    FOREIGN KEY (idReceita) REFERENCES receita(idReceita),
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
);
