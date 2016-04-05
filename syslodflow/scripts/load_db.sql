INSERT INTO pais (nome) VALUES ('Brasil');
INSERT INTO pais (nome) VALUES ('Estados Unidos');
INSERT INTO pais (nome) VALUES ('Alemanha');


INSERT INTO estado (nome,idPais) VALUES ('Santa Catarina',1);
INSERT INTO estado (nome,idPais) VALUES ('Paraná',1);
INSERT INTO estado (nome,idPais) VALUES ('Rio Grande do Sul',1);
INSERT INTO estado (nome,idPais) VALUES ('Rio de Janeiro',1);
INSERT INTO estado (nome,idPais) VALUES ('Distrito Federal',1);
INSERT INTO estado (nome,idPais) VALUES ('Amapá',1);
INSERT INTO estado (nome,idPais) VALUES ('Pará',1);
INSERT INTO estado (nome,idPais) VALUES ('Pernambuco',1);
INSERT INTO estado (nome,idPais) VALUES ('São Paulo',1);
INSERT INTO estado (nome,idPais) VALUES ('Sergipe',1);



INSERT INTO cidade(nome, idEstado) VALUES('Criciúma',1);
INSERT INTO cidade(nome, idEstado) VALUES('Florianópolis',1);
INSERT INTO cidade(nome, idEstado) VALUES('Curitiba',2);

INSERT INTO logradouro(rua, cep, idCidade) VALUES ('Rua Joaquim Nabuco','88803001',1);
INSERT INTO logradouro(rua, cep, idCidade) VALUES ('João Pio Duarte Silva','88803001',2);



INSERT INTO proficiencia (lingua) VALUES ('Inglês');
INSERT INTO proficiencia (lingua) VALUES ('Alemão');
INSERT INTO proficiencia (lingua) VALUES ('Espanhol');
INSERT INTO proficiencia (lingua) VALUES ('Francês');


INSERT INTO revista (nome,ano,qualificao,edicao) VALUES ('Academic Radiology',2014,'A1','324');
INSERT INTO revista (nome,ano,qualificao,edicao) VALUES ('Academy of Management Journal',2015,'A1','334');
INSERT INTO revista (nome,ano,qualificao,edicao) VALUES ('Accounts of Chemical Research',2015,'A1','114');
INSERT INTO revista (nome,ano,qualificao,edicao) VALUES ('ACM Computing Surveys',2013,'A1','111');


INSERT INTO evento(nome,qualificao,ano,idCidade) VALUES ('Secin','A1',2015,3);
INSERT INTO evento(nome,qualificao,ano,idCidade) VALUES ('SBB','A1',2015,1);


INSERT INTO Instituicao(nome, idPais) VALUES('Universidade Federal de Santa Catarina',1);
INSERT INTO Instituicao(nome, idPais) VALUES('Universidade Federal de Santa Maria',1);
INSERT INTO Instituicao(nome, idPais) VALUES('Universidade de São Paulo',1);


INSERT INTO disciplina(ementa,codigo,nome, creditos) VALUES('Introdução a análise e projeto de algoritmos; Complexidade; Notação assintótica; Recorrências;','INE410104','Projeto e Análise de Algoritmos',60);
INSERT INTO disciplina(ementa,codigo,nome, creditos) VALUES('Complexidade; Notação assintótica; Recorrências;','INE410108','Arquitetura de Sistemas Distribuídos',60);
INSERT INTO disciplina(ementa,codigo,nome, creditos) VALUES('Introdução a análise e projeto de algoritmos; Recorrências;','INE6019000','Inteligência Artificial Simbólica',80);
INSERT INTO disciplina(ementa,codigo,nome, creditos) VALUES('Complexidade; Notação assintótica; Recorrências;','INE410095','Sistemas de Tempo Real',40);




INSERT INTO pessoa(nome,telefone,email,cpf,complemento,idLogradouro) VALUES('Gabriela Colonetti','3433-8890','gabriela@gmail.com','08149399982','Res. Fernanda',1);
INSERT INTO pessoa(nome,telefone,email,cpf,complemento,idLogradouro) VALUES('Marina','3433-1190','marina@gmail.com','88849399982','Res. Fernanda',2);
INSERT INTO pessoa(nome,telefone,email,cpf,complemento,idLogradouro) VALUES('Luiza','3431-3390','luiza@gmail.com','2229399982','Res. Fernanda',1);
INSERT INTO pessoa(nome,telefone,email,cpf,complemento,idLogradouro) VALUES('Fernanda','3355-3390','fernanda@gmail.com','4242424','Res. Fernanda',1);
INSERT INTO pessoa(nome,telefone,email,cpf,complemento,idLogradouro) VALUES('Joana','3355-3390','Joana@gmail.com','1222222','Res. Joana',1);



INSERT INTO professor(Vagasmestrado, vagasdoutorado, matriculaProfessor,linkLattes,idCurso,idPessoa) VALUES(10,10,2394943,'http://',1,1);
INSERT INTO professor(Vagasmestrado, vagasdoutorado, matriculaProfessor,linkLattes,idCurso,idPessoa) VALUES(10,10,232323,'http://',2,2);

INSERT INTO curso(nome,idProfessor) VALUES ('Mestrado CCO',1);
INSERT INTO curso(nome,idProfessor) VALUES ('Doutorado CCO',2);


INSERT INTO aluno(matriculaAluno,linkLattes,idCurso,idProfessor,idPessoa) VALUES(2111,'http://',1,2,1);
INSERT INTO aluno(matriculaAluno,linkLattes,idCurso,idProfessor,idPessoa) VALUES(7777,'http://',1,2,2);
INSERT INTO aluno(matriculaAluno,linkLattes,idCurso,idProfessor,idPessoa) VALUES(1222,'http://',1,2,3);


INSERT INTO alunoProficiencia(nota,idAluno, idProficiencia) VALUES(10.00,1,1);
INSERT INTO alunoProficiencia(nota,idAluno, idProficiencia) VALUES(10.00,2,1);

INSERT INTO turma(semestre, idDisciplina, idProfessor) VALUES('2015/1',1,1);
INSERT INTO turma(semestre, idDisciplina, idProfessor) VALUES('2015/1',2,1);
INSERT INTO turma(semestre, idDisciplina, idProfessor) VALUES('2015/1',3,2);
INSERT INTO turma(semestre, idDisciplina, idProfessor) VALUES('2015/1',4,2);


INSERT INTO auxilioEvento(valor, idAluno, idEvento) VALUES(100.00,3,1);
INSERT INTO auxilioEvento(valor, idAluno, idEvento) VALUES(200.00,3,2);
INSERT INTO auxilioEvento(valor, idAluno, idEvento) VALUES(200.00,2,1);
INSERT INTO auxilioEvento(valor, idAluno, idEvento) VALUES(200.00,2,2);


INSERT INTO historico(mediaFinal, descricao, idTurma,idAluno) VALUES(9.00,'aprovado',1,3);
INSERT INTO historico(mediaFinal, descricao, idTurma,idAluno) VALUES(8.00,'aprovado',2,3);
INSERT INTO historico(mediaFinal, descricao, idTurma,idAluno) VALUES(7.00,'aprovado',3,3);
INSERT INTO historico(mediaFinal, descricao, idTurma,idAluno) VALUES(6.00,'aprovado',4,3);
INSERT INTO historico(mediaFinal, descricao, idTurma,idAluno) VALUES(9.00,'aprovado',1,2);
INSERT INTO historico(mediaFinal, descricao, idTurma,idAluno) VALUES(6.00,'aprovado',2,2);
INSERT INTO historico(mediaFinal, descricao, idTurma,idAluno) VALUES(7.00,'aprovado',3,2);
INSERT INTO historico(mediaFinal, descricao, idTurma,idAluno) VALUES(5.00,'reprovado',4,2);



INSERT INTO formacao(ano, nivel, idInstituicao,idProfessor) VALUES(2008,2,1,1);
INSERT INTO formacao(ano, nivel, idInstituicao,idProfessor) VALUES(2014,1,1,2);


INSERT INTO curriculo(semestre, idCurso) VALUES('2015/1',1);
INSERT INTO curriculo(semestre, idCurso) VALUES('2015/1',1);
INSERT INTO curriculo(semestre, idCurso) VALUES('2014/1',2);
INSERT INTO curriculo(semestre, idCurso) VALUES('2014/2',2);



INSERT INTO curriculoDisciplina(idCurriculo, idDisciplina) VALUES(2,1);
INSERT INTO curriculoDisciplina(idCurriculo, idDisciplina) VALUES(2,2);
INSERT INTO curriculoDisciplina(idCurriculo, idDisciplina) VALUES(2,3);
INSERT INTO curriculoDisciplina(idCurriculo, idDisciplina) VALUES(2,4);
INSERT INTO curriculoDisciplina(idCurriculo, idDisciplina) VALUES(4,1);
INSERT INTO curriculoDisciplina(idCurriculo, idDisciplina) VALUES(4,2);
INSERT INTO curriculoDisciplina(idCurriculo, idDisciplina) VALUES(4,3);
INSERT INTO curriculoDisciplina(idCurriculo, idDisciplina) VALUES(4,4);



INSERT INTO publicacao(resumo,titulo,idrevista,idEvento,idAluno) VALUES('resumo resumo resumo','titulo',null,1,3);
INSERT INTO publicacao(resumo,titulo,idrevista,idEvento,idAluno) VALUES('resumo resumo ','titulo',1,null,2);




INSERT INTO defesa(Data,resumo,titulo,conceito) VALUES('2015-10-09','resumo resumo ','titulo1','A');
INSERT INTO defesa(Data,resumo,titulo,conceito) VALUES('2015-08-05','resumo  4','titulo2','A');
INSERT INTO defesa(Data,resumo,titulo,conceito) VALUES('2015-10-03','resumo 3','titulo3','B');
INSERT INTO defesa(Data,resumo,titulo,conceito) VALUES('2015-10-02','resumo resumo 2','titulo4','C');



INSERT INTO coorientacao(idProfessor, idAluno) VALUES(1,1);
INSERT INTO coorientacao(idProfessor, idAluno) VALUES(2,2);



INSERT INTO banca(idDefesa, idProfessor) VALUES(1,1);
INSERT INTO banca(idDefesa, idProfessor) VALUES(1,2);
INSERT INTO banca(idDefesa, idProfessor) VALUES(2,2);

INSERT INTO participacao(idProfessor, idPublicacao) VALUES(2,1);
INSERT INTO participacao(idProfessor, idPublicacao) VALUES(2,2);

INSERT INTO defende(tipo, idDefesa,idAluno) VALUES('Qualificação',1,2);
INSERT INTO defende(tipo, idDefesa,idAluno) VALUES('Tese',2,2);
INSERT INTO defende(tipo, idDefesa,idAluno) VALUES('Dissertação',3,2);





-- grupo dos adminstradores do sistema 

INSERT INTO en_grupo (nome) VALUES ('Administradores');



-- funcoes do sistema (eh atualizado automaticamente)

INSERT INTO en_funcao (nome) VALUES ('USUARIO LEITURA');

INSERT INTO en_funcao (nome) VALUES ('USUARIO ESCRITA');



-- associa todas as funcoes ao grupo de administradores

INSERT INTO en_grupo_funcao (grupo,funcao) VALUES (1,1);

INSERT INTO en_grupo_funcao (grupo,funcao) VALUES (1,2);



-- usuario administrador

INSERT INTO en_usuario (pessoa,login, senha, ativo) VALUES (1,'admin','admin',1);



-- associa usuario 'admin' ao grupo 'administradores do sistemas'

INSERT INTO en_usuario_grupo (usuario,grupo) VALUES (1,1);

