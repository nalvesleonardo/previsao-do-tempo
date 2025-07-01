
-- Garante que estamos usando o banco de dados correto.
USE climadb;

-- Apaga as tabelas antigas, se existirem, para começar do zero.
DROP TABLE IF EXISTS localizacao;
DROP TABLE IF EXISTS dados_horarios;
DROP TABLE IF EXISTS dados_diarios;
DROP TABLE IF EXISTS configuracoes;

CREATE TABLE `localizacao` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cidade` varchar(100) NOT NULL,
  `latitude` decimal(10,6) NOT NULL,
  `longitude` decimal(10,6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Cria a tabela 'dados_diarios'.
CREATE TABLE `dados_diarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_localizacao` int(11) NOT NULL,
  `data` date NOT NULL,
  `temperatura_max` double DEFAULT NULL,
  `temperatura_min` double DEFAULT NULL,
  `precipitacao` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_diarios_localizacao` FOREIGN KEY (`id_localizacao`) REFERENCES `localizacao` (`id`)
) ENGINE=InnoDB;

-- Cria a tabela 'dados_horarios'.
CREATE TABLE `dados_horarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_localizacao` int(11) NOT NULL,
  `horario` datetime NOT NULL,
  `temperatura` double DEFAULT NULL,
  `sensacao_termica` double DEFAULT NULL,
  `precipitacao` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_horarios_localizacao` FOREIGN KEY (`id_localizacao`) REFERENCES `localizacao` (`id`)
) ENGINE=InnoDB;

-- Cria a tabela para as configurações
CREATE TABLE `configuracoes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chave` varchar(100) NOT NULL,
  `valor` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `chave_unica` (`chave`)
) ENGINE=InnoDB;

-- Insere o valor inicial e padrão para a URL da API
INSERT INTO `configuracoes` (chave, valor)
VALUES ('API_BASE_URL', 'https://api.open-meteo.com/v1/forecast')
ON DUPLICATE KEY UPDATE valor=VALUES(valor);

-- Mensagem de sucesso.
SELECT 'Script executado com sucesso! Tabelas recriadas.' AS status;

SHOW TABLES;