//function last(a0 unkaddr, a1 dmaAddr, a2 charcode, a3 screenX)
address=80019724
addiu sp,sp,ffd8
sw s2,18(sp)
sw ra,20(sp)

addu s2,a1,r0	//load sprite head addr
addiu v0,r0,4	//li sprite head
sb v0,3(s2)		//write sprite instructions count

//load rgbSprite and write
lbu t3,40(sp)	//load r
lbu t0,44(sp)	//load g
lbu t4,48(sp)	//load b
sb t3,4(s2)
sb t0,5(s2)
sb t4,6(s2)
addiu v1,r0,64	
sb v1,7(s2)

//load screenXY and write
sh a3,8(s2)
lh t2,38(sp)
sh t2,a(s2)

//build clut and write
srl a1,a2,8		//codeL=code>>8
addiu t0,r0,3	//layer=3
div a1,t0		
mflo t0			//a0=codeL/3
sll t0,t0,4
addiu t0,t0,0380	//base clut.x=896
addiu t1,r0,fc		//clut.y=252
sll v0,t1,6			//function buildclut(t0 clutx, t1 cluty)  return:v0=clut
sra t0,t0,4
andi t0,t0,3f
or v0,v0,t0
andi v0,v0,ffff		//function buildclut() end
sh v0,e(s2)

//build uv and write
mfhi t1			//mod=codeL%3
sll t1,t1,8
andi t2,a2,ff
or t3,t1,t2		//t3=newCharCode=(mod<<8) | (code&0xff)
addiu t1,r0,01b8
subu t2,t3,t1
blez t2,4
addiu t0,t3,0
subu t0,t0,t1	//t0-01b8
addi t0,t0,ffff	//t0-1

addiu t1,r0,15
div t0,t1		//code/21
mfhi t1
sll t2,t1,1
add t1,t2,t1
sll t0,t1,2		//got u
sb t0,c(s2)

mflo t1
sll t2,t1,1
add t1,t2,t1
sll t0,t1,2		//got v
sb t0,d(s2)

//build wh and write
addiu v1,r0,c
sh v1,10(s2)
sh v1,12(s2)

jal 8003f3c8	//call addPrim(a0 addr0, a1 addr1)
add a1,s2,r0

////below are about tp DMA
addiu s2,s2,14
addiu t0,r0,01b8
subu t1,t3,t0
blez t1,3		//if(newcharcode<01b9), skip tp+=1 
addiu v0,r0,e	//tp=e
addiu v0,v0,1	//tp+=1
lui a3,e100
ori a3,a3,0200
or a3,a3,v0		//join e100020e
sw a3,4(s2)		//write "draw mode 00060F TP(?, ?)(bit:?)"
addiu v0,r0,1
sb v0,3(s2)		//write draw mode instructions count
jal 8003f3c8	//call addPrim()
add a1,s2,r0

//final
addiu v0,s2,8
lw ra,20(sp)
lw s2,18(sp)
jr ra
addiu sp,sp,28
