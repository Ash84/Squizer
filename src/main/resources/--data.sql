INSERT INTO user VALUES(0,'$2a$10$obEn8UaeTQViJGY2gM44fOXuxcV36ADuhN6cpQvRpnZ27XWymvzau','Administrateur', 2,'$2a$10$obEn8UaeTQViJGY2gM44fO');
INSERT INTO user VALUES(111,'$2a$10$6efRgg5N/Bjp.Xv6vyF1Ie2OZIL.mWed2BuuhycjwEoWWdSD3C7tS','khalid', 2,'$2a$10$6efRgg5N/Bjp.Xv6vyF1Ie');

INSERT INTO quizzes VALUES(15, 1, 'Exemple');
INSERT INTO questions VALUES(100, 'Des yeux exhorbités !', 'Quel animal a un seul pied', 15, 1, 'bouton');
INSERT INTO questions VALUES(101, 'Vraiment, un indice pour ça?', "Qu'est-ce qui compose l'eau?", 15, 1, 'checkbox');
INSERT INTO questions VALUES(103, 'Avec les majuscules !', 'Quel est le précédent président francais? (prénom nom)', 15, 1, 'texte');

INSERT INTO answers VALUES(101, 'Un escargot', 100, 1, 1);
INSERT INTO answers VALUES(102, 'Un unijambiste', 100, 0, 0);
INSERT INTO answers VALUES(103, 'Le chat', 100, 0, 0);
INSERT INTO answers VALUES(104, 'Emmanuel Macron', 100, 0, 0);
INSERT INTO answers VALUES(105, 'Un éléphant', 100, 0, 0);

INSERT INTO answers VALUES(111, "De l'hydrogène", 101, 1, 1);
INSERT INTO answers VALUES(112, 'De la mémoire', 101, 1, 0);
INSERT INTO answers VALUES(113, "De l'oxygène", 101, 1, 1);
INSERT INTO answers VALUES(114, 'De la chlorure de sodium', 101, 1, 0);
INSERT INTO answers VALUES(115, 'Du carbone', 101, 1, 0);

INSERT INTO answers VALUES(116, 'François Hollande', 103, 1, 1);
INSERT INTO results VALUES(100, 7, 15, 3, 1, 0);
