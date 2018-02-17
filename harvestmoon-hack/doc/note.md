A_FILE.HDT是A_FILE.BIN的入口表,每4字节一组,共36组

每行可容纳18个字
文件3的1900处是开头对话
xxxxF70F:显示头像,xxxx为变化的头像指针

字库分左右两个
文件0的0x7c处开始是右字库:
2byte:图像体大小0x52b0,
2byte:0x400 未压缩数据
4byte:3c0,0 xy
4byte:0x3f,0xa8 代表宽高
0x88处起始,0x52b0大小的图像体数据

紧接着的0x5338处开始是左字库:疑似压缩过
2byte:图像体大小0x7673
2byte:0 压缩数据
4byte:0x380,0 xy
4byte:3f,fc 宽高
9a33~9a39处是像素点,9a56~9a5d是像素点,两处中间的点为压缩数据, 和即时存档中的0x2c6340处对比可看到解压后的数据


对话文本:2,3,4,5,6

日文版拆分后文件结构:
0	=	objcg/gf.bin
1	=	objcg/face.bin
2	=	objcg/message0.bin
3	=	objcg/message1.bin
4	=	objcg/message2.bin
5	=	objcg/message3.bin
6	=	objcg/message4.bin
7	=	data/font/font_all.tex
8	=	obj/debug.bin
9	=	objcg/sound.stm
10	=	obj/pdasamp.bin
11	=	obj/GF_farm.bin
12	=	objcg/event_s.bin
13	=	objcg/evch.bin
14	=	objcg/spring.bin
15	=	objcg/summer.bin
16	=	objcg/fall.bin
17	=	objcg/winter.bin
18	=	objcg/status.bin
19	=	obj/save_load.bin
20	=	objcg/slp.bin
21	=	obj/GF_MG1.bin
22	=	objcg/MG1.bin
23	=	obj/GF_MG2.bin
24	=	objcg/MG2.bin
25	=	obj/GF_MG3.bin
26	=	objcg/MG3.bin
27	=	obj/GF_MG4.bin
28	=	objcg/MG4.bin
29	=	obj/GF_MG5.bin
30	=	objcg/MG5.bin
31	=	obj/GF_TITLE.bin
32	=	obj/GF_STAFF.bin
33	=	objcg/MesEv.bin
34	=	obj/GF_swind.bin
35	=	obj/gf_mcard.bin


英文版拆分后文件结构:
0	=	objcg/gf.bin
1	=	objcg/face.bin
2	=	objcg/message0.bin
3	=	objcg/message1.bin
4	=	objcg/message2.bin
5	=	objcg/message3.bin
6	=	objcg/message4.bin
7	=	data/font/font_all.tex
8	=	obj/debug.bin
9	=	objcg/sound.stm
10	=	obj/pdasamp.bin
11	=	obj/GF_farm.bin
12	=	objcg/event_s.bin
13	=	objcg/evch.bin
14	=	objcg/status.bin
15	=	obj/save_load.bin
16	=	objcg/slp.bin
17	=	obj/GF_MG1.bin
18	=	objcg/MG1.bin
19	=	obj/GF_MG2.bin
20	=	objcg/MG2.bin
21	=	obj/GF_MG3.bin
22	=	objcg/MG3.bin
23	=	obj/GF_MG4.bin
24	=	objcg/MG4.bin
25	=	obj/GF_MG5.bin
26	=	objcg/MG5.bin
27	=	obj/GF_TITLE.bin
28	=	obj/GF_STAFF.bin
29	=	objcg/MesEv.bin
30	=	obj/GF_swind.bin
31	=	obj/gf_mcard.bin