SELECT * FROM logger.logValues;CREATE TABLE `logValues` (
  `idlogValues` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(45) NOT NULL,
  `logType` int(11) NOT NULL,
  PRIMARY KEY (`idlogValues`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;