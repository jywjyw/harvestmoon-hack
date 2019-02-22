========内存规划==========
80000000 ~ 80060000: EXE
80060578~?: 显存upload中转区,每一行图像被解压至此,再upload至显存
801C8440 ~ 801d4c40:CD数据加载至此,再解压至8006xxxx 


========字库==================
字库分左右两个,共735字
文件0的0x7c处开始是右字库,共21*14=294字:
3byte:图像体大小0x0052b0,
1byte:
4byte:3c0,0 xy
4byte:0x3f,0xa8 代表宽高63,168
0x88处起始,0x52b0大小的图像体数据

紧接着的0x5338处开始是左字库,共21*21=441字
3byte:图像体大小0x7673
1byte:
4byte:0x380,0 xy
4byte:3f,fc 宽高63,252
9a33~9a39处是像素点,9a56~9a5d是像素点,两处中间的点为压缩数据, 和即时存档中的0x2c6340处对比可看到解压后的数据

字库所用色板:(896,254) (944,254)
(896,252)一整行无图像,可以用来存放4个色板




0exx:??
0fxx:控制符
2xxx:
4xxx:
8xxx:
fffx:控制符

无需隔离的控制符:boy/farm/dog

=======开始文本===============
「ミネラルタウン」へようこそ！
わたしが町長のト-マスです。
これからいくつかしつもんをするので
こたえてください。
位于8006e4a0处

每行可容纳18个字,继续变大则超出对话框背景, 达到23个字时则因超出显存范围而死机
文件3的1900处是开头对话
xxxxF70F:显示头像,xxxx为变化的头像指针


============A_FILE.BIN===================================
A_FILE.HDT是A_FILE.BIN的索引表,每4字节一组,共36组,将A_FILE.BIN拆解成36个文件,目录如下
0=objcg/gf.bin
1=objcg/face.bin
2=objcg/message0.bin
3=objcg/message1.bin
4=objcg/message2.bin
5=objcg/message3.bin
6=objcg/message4.bin
7=data/font/font_all.tex
8=obj/debug.bin
9=objcg/sound.stm
10=obj/pdasamp.bin
11=obj/gf_farm.bin
12=objcg/event_s.bin
13=objcg/evch.bin
14=objcg/spring.bin
15=objcg/summer.bin
16=objcg/fall.bin
17=objcg/winter.bin
18=objcg/status.bin
19=obj/save_load.bin
20=objcg/slp.bin
21=obj/GF_MG1.bin
22=objcg/MG1.bin
23=obj/GF_MG2.bin
24=objcg/MG2.bin
25=obj/GF_MG3.bin
26=objcg/MG3.bin
27=obj/GF_MG4.bin
28=objcg/MG4.bin
29=obj/GF_MG5.bin
30=objcg/MG5.bin
31=obj/GF_TITLE.bin
32=obj/GF_STAFF.bin
33=objcg/MesEv.bin
34=obj/GF_swind.bin
35=obj/gf_mcard.bin



