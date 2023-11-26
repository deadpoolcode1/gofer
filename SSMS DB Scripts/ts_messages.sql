USE [GOPHER]
GO

/****** Object:  Table [dbo].[ts_messages]    Script Date: 28/03/2022 17:17:52 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[ts_messages](
	[ser_num] [int] NOT NULL,
	[read] [int] NULL,
	[sender_host] [int] NULL,
	[sender_pr] [int] NULL,
	[dest_host] [int] NULL,
	[dest_pr] [int] NULL,
	[time] [int] NULL,
	[msg_code] [int] NULL,
	[length] [int] NULL,
 CONSTRAINT [PK_ts_messages] PRIMARY KEY CLUSTERED 
(
	[ser_num] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

