CREATE DATABASE `logger` /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE TABLE `logValues` (
  `idlogValues` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(45) NOT NULL,
  `logType` int(11) NOT NULL,
  PRIMARY KEY (`idlogValues`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;