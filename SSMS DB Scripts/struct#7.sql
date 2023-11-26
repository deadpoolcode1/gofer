USE [GOPHER]
GO

/****** Object:  Table [dbo].[struct#7]    Script Date: 28/03/2022 17:18:20 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[struct#7](
	[ser_num] [int] NOT NULL,
	[text:byte20] [varchar](20) NULL,
 CONSTRAINT [PK_struct#7] PRIMARY KEY CLUSTERED 
(
	[ser_num] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

