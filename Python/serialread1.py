import serial
ser = serial.Serial ("/dev/ttyAMA0")
ser.baudrate = 9600
print 'Reading'
while True:
	data=ser.read(1)
	if data=='U':
		dist=ser.readline()
		print 'Ultrasonic dsitance (cm): ',dist
		file=open("ultra.txt","w")
		file.write(dist);
		file.close()
ser.close()
