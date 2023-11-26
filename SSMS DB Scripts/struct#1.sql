USE [GOPHER]
GO

/****** Object:  Table [dbo].[struct#1]    Script Date: 28/03/2022 17:18:31 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[struct#1](
	[ser_num] [int] NOT NULL,
	[f1:float32] [float] NULL,
	[int1:int32] [int] NULL,
	[int2:int32] [int] NULL,
	[bytes:byte20] [varchar](20) NULL,
	[bool1:boolean] [int] NULL,
 CONSTRAINT [PK_struct#1] PRIMARY KEY CLUSTERED 
(
	[ser_num] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

