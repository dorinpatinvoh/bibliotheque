-- Les mots de passe sont "password" avant encodage. 
-- Spring Security les encodera au démarrage si vous utilisez AuthenticationManagerBuilder
-- ou vous pouvez pré-encoder et insérer la version encodée.
-- Pour cet exemple, nous laissons UserService gérer l'encodage à l'enregistrement.
-- On peut ajouter des utilisateurs par l'API /auth/register.

INSERT INTO livres (title, author, available) VALUES ('Le Seigneur des Anneaux', 'J.R.R. Tolkien', true);
INSERT INTO livres (title, author, available) VALUES ('1984', 'George Orwell', true);
INSERT INTO livres (title, author, available) VALUES ('Le Petit Prince', 'Antoine de Saint-Exupéry', true);
INSERT INTO livres (title, author, available) VALUES ('Fahrenheit 451', 'Ray Bradbury', false); -- Un livre déjà emprunté pour test