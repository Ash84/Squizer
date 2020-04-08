DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `quizzes`;
DROP TABLE IF EXISTS `results`;
DROP TABLE IF EXISTS `questions`;
DROP TABLE IF EXISTS `answers`;

CREATE TABLE `user`(
  `user_id` int(11) NOT NULL,
  `hashed_password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `quizzes`(
  `quiz_id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `state` int(1) DEFAULT NULL,
  PRIMARY KEY (`quiz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `results`(
  `user_id` int(11) NOT NULL,
  `quiz_id` int(11) DEFAULT NULL,
  `result_id` int(11) DEFAULT NULL,
  `score` int(2) DEFAULT NULL,
  `tries` int(1) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `questions`(
  `question_id` int(11) NOT NULL,
  `quiz_id` int(11) DEFAULT NULL,
  `question` varchar(255) DEFAULT NULL,
  `hint` varchar(255) DEFAULT NULL,
  `state` int(1) DEFAULT NULL,
  PRIMARY KEY (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `answers`(
  `answer_id` int(11) NOT NULL,
  `question_id` int(11) DEFAULT NULL,
  `answer` varchar(255) DEFAULT NULL,
  `state` int(1) DEFAULT NULL,
  PRIMARY KEY (`answer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
