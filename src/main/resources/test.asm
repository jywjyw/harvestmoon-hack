//v0=code
address=80000000
srl t1,v0,8
addiu t2,r0,3
div t1,t2
mfhi t1				//t1=layer
mflo t2	
sll t2,t2,8
andi v0,v0,ff
or v0,t2,v0
sltiu t2,v0,01B9
bne t2,r0,5
addiu t2,r0,380		//t2=TP.X=896
addiu t2,t2,40		//t2=TP.X=960
addiu t3,r0,01B9
subu v0,v0,t3
addiu t3,r0,15		//loc
div v0,t3
mfhi t4
sll t5,t4,3
sll t4,t4,2
add t3,t4,t5				
mflo t4
sll t5,t4,3
sll t4,t4,2
add t4,t4,t5		
sll t3,t3,8
or t3,t3,t4			//t3=uv		