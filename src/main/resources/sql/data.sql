INSERT INTO t_group VALUES (1,'root'),(2,'user');
INSERT INTO t_user VALUES (1,'root','root','admin@indizen.com',0,1,0,0,'root','root','4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2',null,'2012-01-01','2012-01-01'),(2,'user','user','user@indizen.com',0,1,0,0,'user','user','04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb',null,'2012-01-01','2012-01-01');
INSERT INTO t_permission VALUES (1,'ROLE_ACCESO_SISTEMA'),(2,'ROLE_SUPER_USUARIO'),(3,'ROLE_USUARIO');
INSERT INTO t_user_group VALUES (1,1,1),(2,2,2);
INSERT INTO t_group_permission VALUES (1,1,1),(2,1,2),(3,2,3),(4,2,1);
