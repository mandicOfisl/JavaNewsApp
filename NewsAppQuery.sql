use master
go

create database JavaNewsApp
go

use JavaNewsApp
go

alter database current collate Croatian_CS_AI_KS_WS;
go

create table [User] (
	IdUser int primary key identity,
	[Name] nvarchar(50),
	Pass nvarchar(50),
	IsAdmin bit)
go

insert into [User] values
	('admin', 'guest', 1)
go

create table Category (
	IdCategory int primary key identity,
	[Name] nvarchar(50))
go

create table Creator (
	IdCreator int primary key identity,
	[Name] nvarchar(100))
go

create table Article (
	IdArticle int primary key identity,
	Title nvarchar(300),
	Link nvarchar(300),
	PicturePath nvarchar(300),
	[Description] nvarchar(900),
	Content nvarchar(max),
	CreatorId int foreign key references Creator(IdCreator),
	PublishedDate nvarchar(90),
	CategoryId int foreign key references Category(IdCategory))
go

create table UserArticle (
	IdUserArticle int primary key identity,
	UserId int foreign key references [User](IdUser),
	ArticleId int foreign key references Article(IdArticle))
go


create proc createUser
	@Name nvarchar(50),
	@Pass nvarchar(50),
	@Id int output
as
begin
	insert into [User] values
		(@Name, @Pass, 0)
	set @Id = SCOPE_IDENTITY()
end
go

create proc getUserByName
	@Name nvarchar(100)
as
begin
	select *
	from [User] as u
	where u.Name = @Name
end
go

create proc createCreator
	@Name nvarchar(100),
	@Id int output
as
begin
	insert into Creator values
		(@Name)
	set @Id = SCOPE_IDENTITY()
end
go

create proc getCreators
as
begin
	select * from Creator
end
go

create proc updateCreator
	@Id int,
	@Name nvarchar(100)
as
begin
	update Creator
	set [Name] = @Name
	where IdCreator = @Id
end
go

create proc deleteCreator
	@Id int
as
begin
	update Article
	set CreatorId = null
	where CreatorId = @Id
	delete from Creator
	where IdCreator = @Id
end
go

create proc createCategory
	@Name nvarchar(50),
	@Id int output
as
begin
	insert into Category values
		(@Name)
	set @Id = SCOPE_IDENTITY()
end
go

create proc getCategories
as
begin
	select * from Category
end
go

create proc updateCategory
	@Id int,
	@Name nvarchar(50)
as
begin
	update Category
	set [Name] = @Name
	where IdCategory = @Id
end
go

create proc deleteCategory
	@Id int
as
begin
	update Article
	set CategoryId = null
	where CategoryId = @Id
	delete from Category
	where IdCategory = @Id
end
go

create proc createArticle
	@Title nvarchar(300),
	@Link nvarchar(300),
	@PicturePath nvarchar(300),
	@Description nvarchar(900),
	@Content nvarchar(max),
	@Creator int,
	@PublishedDate nvarchar(90),
	@Category int,
	@Id int output
as
begin
	insert into Article values (
		@Title, 
		@Link, 
		@PicturePath, 
		@Description, 
		@Content,
		@Creator, 
		@PublishedDate,
		@Category)
	set @Id = SCOPE_IDENTITY()
end
go

create proc selectArticle
	@Id int
as
begin
	select a.IdArticle, a.Title, a.Link, a.PicturePath, a.Description, a.Content, cr.Name as [Creator], a.PublishedDate, cat.Name as [Category] 
	from Article as a
	left join Creator as cr on a.CreatorId = cr.IdCreator
	left join Category as cat on a.CategoryId = cat.IdCategory
	where a.IdArticle = @Id
end
go

create proc selectArticles
as
begin
	select a.IdArticle, a.Title, a.Link, a.PicturePath, a.Description, a.Content, cr.Name as [Creator], a.PublishedDate, cat.Name as [Category] 
	from Article as a
	left join Creator as cr on a.CreatorId = cr.IdCreator
	left join Category as cat on a.CategoryId = cat.IdCategory
end
go

create proc updateArticle
	@Id int,
	@Title nvarchar(300),
	@Link nvarchar(300),
	@PicturePath nvarchar(300),
	@Description nvarchar(900),
	@Content nvarchar(max),
	@Creator nvarchar(100),
	@PublishedDate nvarchar(90),
	@Category nvarchar(50)
as
begin
	update Article
	set
		Title = @Title,
		Link = @Link,
		PicturePath = @PicturePath,
		Description = @Description,
		Content = @Content,
		CreatorId = (
			select IdCreator
			from Creator
			where [Name] = @Creator),
		PublishedDate = @PublishedDate,
		CategoryId = (
			select IdCategory
			from Category
			where [Name] = @Category)
	where IdArticle = @Id
end
go

create proc deleteArticle
	@Id int
as
begin
delete from UserArticle
	where ArticleId = @Id
	delete from Article
	where Article.IdArticle = @Id
end
go

create proc deleteArticles
as
begin
	delete from UserArticle
	delete from Article
end
go

create proc selectFavouriteArticles
	@UserId int
as
begin
	select a.IdArticle, a.Title, a.Link, a.PicturePath, a.Description, a.Content, cr.Name as [Creator], a.PublishedDate, cat.Name as [Category] 
	from Article as a
	left join Creator as cr on a.CreatorId = cr.IdCreator
	left join Category as cat on a.CategoryId = cat.IdCategory	
	where a.IdArticle in (
		select ua.ArticleId
		from UserArticle as ua
		where ua.UserId = @UserId)
end
go

create proc addFavouriteArticle
	@UserId int,
	@ArticleId int
as
begin
	insert into UserArticle values (@UserId, @ArticleId)
end
go

create proc removeFavouriteArticle
	@UserId int,
	@ArticleId int
as
begin
	delete from UserArticle
	where UserId = @UserId and ArticleId = @ArticleId
end
go

create proc deleteAllData
as
begin
	delete from UserArticle
	delete from Article
	delete from Category	
	delete from Creator
	delete from [User]
	where [User].[Name] != 'admin'
end
go