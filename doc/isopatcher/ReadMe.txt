
                 iso patcher v0.5
                 =================

  iso patcher ��һ��������д��iso�ĳ���
  ֧��iso9660���ָ�ʽ��PS, SS����iso.
  ֧���Զ��������У�顣

  ����Ŀ��Դ����ѭ GNU GPL �淶�����������κ���ҵ��;�ȡ�
  Դ���������vc6�Լ�VC.NET���롣������иĽ������ύ�����ǡ�


*************************************************************
 ��ʷ�汾 History of changes:

 2004/12/27 v0.5 - Agemo
 - ���Ӱ�ԭʼ����д��ָ��λ�õĹ���

 2004/04/18 v0.4 - Agemo
 - code cleanup
 - ���� mode1 ֧�֣�mode2 form2 ֧�֣�δ���ԣ�
 - ecc edc �����㷨���û��汾������C���԰�
 - ����win32 dll���֧�֣�Ϊ����������ֱ�ӵ���
 - ע�͵��б�Ǽ�����#

 2004/01/15 v0.3 - xade
 - LST ��ʽ������ÿ���Ϊһ��
 - LST ��ʽ����ֱ��д���ֽڵ�֧�֣������Ǳ����ⲿ���ļ���
 - LST ���Կ���

 2002/11/06 v0.2 - xade
 - ��ǿ�˶� LST �ļ��Ĵ����⣬��������һ���ڼ�� LST
   �ļ�ʱ��һЩС���⣬��Ȼ���ǲ���Ӱ��ʹ��^^
 - ������ע�͵�֧�֣��԰�Ƿֺ�;��ͷ����Ϊע���С�
 - �����Զ����� EDC/ECC �Ĺ��ܣ����������� patch ��ʹ��
   ECCRegen ֮���������������ˣ�Yeah~~~~��^^
   �����ÿ��� /e �ر��������:)

 2002/10/16 v0.1 - xade
 - ���ں���������ҹ����������Ҫ�����Լ�д�����С���ˣ�
   �ṩ������� patch ���ܣ�����ֻ������ playstation
   ���̵� mode 2 form 1 ���ݣ����������޸� XA ��ʽ�����ݡ�

*************************************************************
  ʹ�÷�����

  exe ����Ҫ��window����������ʾ��������:
  isopatch list_file iso_file [/mode] [/e]

  /mode : iso mode, optional params.
          /M1   = mode1
          /M2F1 = mode2 form1 (default) (playstation)
          /M2F2 = mode2 form2
  /e: calculating ecc and edc OFF. optional params. (default ON)

  all params are case insensitive.

*************************************************************

�������ָ�ʽ��ÿ������2352�ֽڣ�ʵ��������2048�ֽڣ�������У��ȡ�

���²��裺
01. ����Ҫ�޸ĵ���������������ڸ��Ƴ�����
    ���߰�ֱ�Ӵ�iso�ڸ��Ƴ���������Ҫ���Ƶı��������������ڵĲ��֡�
    Ȼ�������ʮ�����Ʊ༭���޸ġ�

02. ��ʮ�����Ʊ༭���ж� ISO ����������ݵ���ʼ��ַ��
    �������������� 0x0005f04c

03. �½�һ���ı��ļ�����֮Ϊpatch list�ļ���
    ������ϸ��˵�����Ӽ� example\list.txt��

    ������ʼ��ַ���޸ĺ�������ļ�����������ľ���������
        0005f04c,holybell.bin
    Ȼ�󱣴�����ļ������籣��Ϊ kill.lst

04. ��������������֣��ظ�01-03�Ĺ���

05. �������漰�����ļ���holybell.bin ����ͬ��� kill.lst����������
    Ŀ�꾵���ļ�������Ϊ dark01.bin ������ͬһ��Ŀ¼�£�
    Ȼ��ִ���������
        isopatch kill.lst dark01.bin
    
    ������������Ҫ����Ҫ���� EDC/ECC �Ļ���ִ���������
        isopatch kill.lst dark01.bin /e

    �����������Ҫ������mode1 iso�ģ�
        isopatch kill.lst dark01.bin /M1

=======================================================
1.rename <harvest moon> iso to "harvest-hack.iso", put it into this directory
2.put A_FILE.bin into this directory
3.run java code, modify A_FILE.bin, generate A_FILE.HDT
4.run "isopatch.exe harvest.lst harvest-hack.iso" in command line
