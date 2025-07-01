
-- 1. Garante que estamos usando o banco de dados correto.
USE climadb;

-- 2. Apaga as tabelas antigas, se existirem, para começar do zero.
DROP TABLE IF EXISTS dados_horarios;
DROP TABLE IF EXISTS dados_diarios;

-- 3. Recria a tabela 'dados_diarios'.
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

-- 4. Recria a tabela 'dados_horarios'.
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

-- 5. Mensagem de sucesso.
SELECT 'Script executado com sucesso! Tabelas recriadas.' AS status;

SHOW TABLES;