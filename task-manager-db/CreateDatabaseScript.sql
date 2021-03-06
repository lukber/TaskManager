USE [master]
GO
CREATE DATABASE [TaskManager] ON  PRIMARY 
( NAME = N'TaskManager', FILENAME = N'C:\TaskManager\db\TaskManager.mdf' , SIZE = 3072KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'TaskManager_log', FILENAME = N'C:\TaskManager\db\TaskManager_log.ldf' , SIZE = 1024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
 COLLATE Czech_CI_AS
GO
EXEC dbo.sp_dbcmptlevel @dbname=N'TaskManager', @new_cmptlevel=90
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [TaskManager].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [TaskManager] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [TaskManager] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [TaskManager] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [TaskManager] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [TaskManager] SET ARITHABORT OFF 
GO
ALTER DATABASE [TaskManager] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [TaskManager] SET AUTO_CREATE_STATISTICS ON 
GO
ALTER DATABASE [TaskManager] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [TaskManager] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [TaskManager] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [TaskManager] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [TaskManager] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [TaskManager] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [TaskManager] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [TaskManager] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [TaskManager] SET  DISABLE_BROKER 
GO
ALTER DATABASE [TaskManager] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [TaskManager] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [TaskManager] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [TaskManager] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [TaskManager] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [TaskManager] SET  READ_WRITE 
GO
ALTER DATABASE [TaskManager] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [TaskManager] SET  MULTI_USER 
GO
ALTER DATABASE [TaskManager] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [TaskManager] SET DB_CHAINING OFF 
GO
IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = N'taskManager_app')
CREATE LOGIN [taskManager_app] WITH PASSWORD=N'taskManager_app', DEFAULT_DATABASE=[TaskManager], DEFAULT_LANGUAGE=[čeština], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF
GO
ALTER LOGIN [taskManager_app] ENABLE
GO
USE [TaskManager]
GO
IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = N'taskManager_app')
CREATE USER [taskManager_app] FOR LOGIN [taskManager_app] WITH DEFAULT_SCHEMA=[dbo]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
EXEC sp_addrolemember db_datareader, taskManager_app
GO
EXEC sp_addrolemember db_datawriter, taskManager_app
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[LOGIN]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[LOGIN](
	[LOGIN_ID] [int] IDENTITY(1,1) NOT NULL,
	[LOGIN_NAME] [nvarchar](50) NOT NULL,
	[LOGIN_USR_NAME] [nvarchar](50) NOT NULL,
	[LOGIN_USR_PASS] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_LOGIN] PRIMARY KEY CLUSTERED 
(
	[LOGIN_ID] ASC
)WITH (PAD_INDEX  = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UNIQ_LOGIN] UNIQUE NONCLUSTERED 
(
	[LOGIN_USR_NAME] ASC
)WITH (PAD_INDEX  = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
END
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'LOGIN', N'COLUMN',N'LOGIN_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID uzivatele' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'LOGIN', @level2type=N'COLUMN',@level2name=N'LOGIN_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'LOGIN', N'COLUMN',N'LOGIN_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Nazev uzivatele' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'LOGIN', @level2type=N'COLUMN',@level2name=N'LOGIN_NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'LOGIN', N'COLUMN',N'LOGIN_USR_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Uzivatelske jmeno pro prihlaseni do aplikace' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'LOGIN', @level2type=N'COLUMN',@level2name=N'LOGIN_USR_NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'LOGIN', N'COLUMN',N'LOGIN_USR_PASS'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Uzivatelske heslo pro prihlaseni do aplikace' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'LOGIN', @level2type=N'COLUMN',@level2name=N'LOGIN_USR_PASS'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'LOGIN', NULL,NULL))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Uživatel' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'LOGIN'
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[TSK_CATEG]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[TSK_CATEG](
	[TSK_CATEG_ID] [int] IDENTITY(1,1) NOT NULL,
	[TSK_CATEG_NAME] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_TSK_CATEG] PRIMARY KEY CLUSTERED 
(
	[TSK_CATEG_ID] ASC
)WITH (PAD_INDEX  = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UNIQ_TSK_CATEG] UNIQUE NONCLUSTERED 
(
	[TSK_CATEG_NAME] ASC
)WITH (PAD_INDEX  = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
END
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK_CATEG', N'COLUMN',N'TSK_CATEG_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID kategorie' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK_CATEG', @level2type=N'COLUMN',@level2name=N'TSK_CATEG_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK_CATEG', N'COLUMN',N'TSK_CATEG_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Nazev kategorie' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK_CATEG', @level2type=N'COLUMN',@level2name=N'TSK_CATEG_NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK_CATEG', NULL,NULL))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Kategorie' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK_CATEG'
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[TSK]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[TSK](
	[TSK_ID] [int] IDENTITY(1,1) NOT NULL,
	[TSK_CATEG_ID] [int] NOT NULL,
	[TSK_NAME] [nvarchar](50) NOT NULL,
	[TSK_DESK] [nvarchar](max) NULL,
	[TSK_FINISH_TO_DATE] [datetime] NULL,
	[TSK_FINISHED_DATE] [datetime] NULL,
	[TSK_LOGIN_ID_EXECUTOR] [int] NOT NULL,
	[TSK_LOGIN_ID_INS] [int] NOT NULL,
	[TSK_INS_DATE] [datetime] NOT NULL CONSTRAINT [DF_TSK_TSK_INS_DATE]  DEFAULT (getdate()),
	[TSK_UPD_DATE] [datetime] NOT NULL CONSTRAINT [DF_TSK_TSK_UPD_DATE]  DEFAULT (getdate()),
	[TSK_UNREAD] [bit] NOT NULL CONSTRAINT [DF_TSK_TSK_UNREAD]  DEFAULT ((1)),
 CONSTRAINT [PK_TSK] PRIMARY KEY CLUSTERED 
(
	[TSK_ID] ASC
)WITH (PAD_INDEX  = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
END
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK', N'COLUMN',N'TSK_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID ukolu' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK', @level2type=N'COLUMN',@level2name=N'TSK_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK', N'COLUMN',N'TSK_CATEG_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID kategorie ukolu' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK', @level2type=N'COLUMN',@level2name=N'TSK_CATEG_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK', N'COLUMN',N'TSK_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Nazev ukolu' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK', @level2type=N'COLUMN',@level2name=N'TSK_NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK', N'COLUMN',N'TSK_DESK'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Popis ukolu' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK', @level2type=N'COLUMN',@level2name=N'TSK_DESK'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK', N'COLUMN',N'TSK_FINISH_TO_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Do kdy ma byt ukol dokoncen' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK', @level2type=N'COLUMN',@level2name=N'TSK_FINISH_TO_DATE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK', N'COLUMN',N'TSK_FINISHED_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Kdy byl ukol dokoncen' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK', @level2type=N'COLUMN',@level2name=N'TSK_FINISHED_DATE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK', N'COLUMN',N'TSK_LOGIN_ID_EXECUTOR'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Kdo ma ukol splnit' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK', @level2type=N'COLUMN',@level2name=N'TSK_LOGIN_ID_EXECUTOR'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK', N'COLUMN',N'TSK_LOGIN_ID_INS'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Kdo ukol zalozil' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK', @level2type=N'COLUMN',@level2name=N'TSK_LOGIN_ID_INS'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK', N'COLUMN',N'TSK_INS_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Datum zalozeni ukolu' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK', @level2type=N'COLUMN',@level2name=N'TSK_INS_DATE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'TSK', N'COLUMN',N'TSK_UPD_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Datum uprav ukolu' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TSK', @level2type=N'COLUMN',@level2name=N'TSK_UPD_DATE'
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_TSK_LOGIN_EXECUTOR]') AND parent_object_id = OBJECT_ID(N'[dbo].[TSK]'))
ALTER TABLE [dbo].[TSK]  WITH CHECK ADD  CONSTRAINT [FK_TSK_LOGIN_EXECUTOR] FOREIGN KEY([TSK_LOGIN_ID_EXECUTOR])
REFERENCES [dbo].[LOGIN] ([LOGIN_ID])
GO
ALTER TABLE [dbo].[TSK] CHECK CONSTRAINT [FK_TSK_LOGIN_EXECUTOR]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_TSK_LOGIN_INS]') AND parent_object_id = OBJECT_ID(N'[dbo].[TSK]'))
ALTER TABLE [dbo].[TSK]  WITH CHECK ADD  CONSTRAINT [FK_TSK_LOGIN_INS] FOREIGN KEY([TSK_LOGIN_ID_INS])
REFERENCES [dbo].[LOGIN] ([LOGIN_ID])
GO
ALTER TABLE [dbo].[TSK] CHECK CONSTRAINT [FK_TSK_LOGIN_INS]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_TSK_TSK_CATEG]') AND parent_object_id = OBJECT_ID(N'[dbo].[TSK]'))
ALTER TABLE [dbo].[TSK]  WITH CHECK ADD  CONSTRAINT [FK_TSK_TSK_CATEG] FOREIGN KEY([TSK_CATEG_ID])
REFERENCES [dbo].[TSK_CATEG] ([TSK_CATEG_ID])
GO
ALTER TABLE [dbo].[TSK] CHECK CONSTRAINT [FK_TSK_TSK_CATEG]
GO

/* Pridani zaznamu */
INSERT INTO [TaskManager].[dbo].[LOGIN]
           ([LOGIN_NAME]
           ,[LOGIN_USR_NAME]
           ,[LOGIN_USR_PASS])
     VALUES
           ('Petr'
           ,'petr'
           ,'rZG7Pn6daiQnnLeC+Nh4uuEUids=')
GO
INSERT INTO [TaskManager].[dbo].[LOGIN]
           ([LOGIN_NAME]
           ,[LOGIN_USR_NAME]
           ,[LOGIN_USR_PASS])
     VALUES
           ('Martin'
           ,'martin'
           ,'rZG7Pn6daiQnnLeC+Nh4uuEUids=')
GO
INSERT INTO [TaskManager].[dbo].[LOGIN]
           ([LOGIN_NAME]
           ,[LOGIN_USR_NAME]
           ,[LOGIN_USR_PASS])
     VALUES
           ('Tomas'
           ,'tomas'
           ,'rZG7Pn6daiQnnLeC+Nh4uuEUids=')
GO
INSERT INTO [TaskManager].[dbo].[TSK_CATEG]
           ([TSK_CATEG_NAME])
     VALUES
           ('Pracovni')
GO
INSERT INTO [TaskManager].[dbo].[TSK_CATEG]
           ([TSK_CATEG_NAME])
     VALUES
           ('Domaci')
GO
