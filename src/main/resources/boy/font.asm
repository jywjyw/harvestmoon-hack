//function last(a1 unkAddr, a2 charcode, a3 screenX, &clutX, &clutY, &TP), about 140 instructions
address=8001a34c
addiu sp,sp,ffc0
sw s0,18(sp)
sw s1,1c(sp)
sw s2,20(sp)
sw s3,24(sp)
sw s4,28(sp)
sw s5,2c(sp)
sw s6,30(sp)
sw s7,34(sp)
sw fp,38(sp)
sw ra,3c(sp)
sw a0,40(sp)

lbu s0,68(sp)	//related to 8001a478
lw s1,58(sp)	//load rgb1
lw s2,5c(sp)	//load rgb2
lw s3,60(sp)	//load rgb3

lhu t0,50(sp)	//load screen.y
addu s6,a3,r0	//move screenX to s6
sh t0,10(sp)	//move screen.y to another addr

//about s4 data, unknown
addu s4,a1,r0	
addiu v0,r0,4
sb v0,3(s4)		//??
addiu v0,r0,64
sb v0,7(s4)		//save sprite code


srl t1,a2,8		//codeL=code>>8
addiu t2,r0,3	//layer=3
div t1,t2		
mflo a0			//a0=codeL/3
sll a0,a0,4
addiu a0,a0,0380	//base clut.x=896
jal 8004043c		//call GetClut(a0 clutX, a1 clutY). return v0=clut. modify reg : v0 a0. 
addiu a1,r0,fc		//clut.y=252

mfhi t1			//mod=codeL%3
sll t1,t1,8
andi t2,a2,ff
or s5,t1,t2		//s5=newCharCode=(mod<<8) | (code&0xff)
addiu t1,r0,01b8
subu t2,s5,t1
blez t2,4
addiu t0,s5,0
subu t0,t0,t1	//t0-01b8
addi t0,t0,ffff	//t0-1

addiu t1,r0,15
div t0,t1		//code/21
mfhi t1
sll t2,t1,1
add t1,t2,t1
sll s7,t1,2		//s7=u

mflo t1
sll t2,t1,1
add t1,t2,t1
sll fp,t1,2		//fp=v


//just copy 8001a454~801a4bc
andi s0,s0,ff
sh v0,e(s4)
sb s1,4(s4)
sb s2,5(s4)
beq s0,r0,5
sb s3,6(s4)

lb v0,7(s4)
j 8001a484
ori v0,v0,2

lb v0,7(s4)
nop
andi v0,v0,fd

sb v0,7(s4)
lui v0,8005
addiu v0,v0,b538
sll s0,s5,1
addu s0,s0,s5
sll s0,s0,2
addu s0,s0,v0
addu a1,s4,r0
lw a0,40(sp)
//lhu a2,8(s0)
//lhu a3,3(s0)
addiu v0,r0,c
sh s6,8(s4)
lhu t0,10(sp)
andi v1,s7,ffff
sh t0,a(s4)
sh v0,10(s4)
sh v0,12(s4)
//end of copy

//addu a2,a2,s7	//u += a2Offset
//addu a3,a3,fp	//v += a3Offset
sb s7,c(s4)		//write u
jal 8004045c	//call addPrim(a0, a1). modify reg: a1 a2 a3 v0 v1
sb fp,d(s4)		//write v

//just copy 8001a4fc~8001a514
addiu s4,s4,14
lui a3,e100
ori a3,a3,600
addiu v0,r0,1
lw a0,40(sp)
addu a1,s4,r0
sb v0,3(s4)
//copy end

addiu t0,r0,01b8
subu t1,s5,t0
blez t1,3		//if(t1<01b9), skip tp+=1 
addiu v0,r0,e	//tp=e
addiu v0,v0,1	//tp+=1
or v0,v0,a3		//v0 = e1000600 | tp
jal 8004045c	//call addPrim()
sw v0,4(s4)		//write "draw mode 00060F TP(?, ?)(bit:?)"

addiu v0,s4,c
//restore context
lw ra,3c(sp)
lw fp,38(sp)
lw s7,34(sp)
lw s6,30(sp)
lw s5,2c(sp)
lw s4,28(sp)
lw s3,24(sp)
lw s2,20(sp)
lw s1,1c(sp)
lw s0,18(sp)
jr ra
addiu sp,sp,40