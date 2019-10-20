
//#include <LiquidCrystal.h>
//LiquidCrystal lcd(4, 2, 3, 7, 8, 9);

#define M0 5
#define M1 A5
#define M2 6
#define M3 A4

#define trigPin1 8 // Trigger Pin
#define echoPin1 9 // Echo Pin

unsigned long previousMillis=0;

char incomingByte;

long distance1;
int fwd_flag=0;
//*************************************************
void read1_hcsr04()
{
  long duration;


  digitalWrite(trigPin1, LOW); 
  delayMicroseconds(2); 

  digitalWrite(trigPin1, HIGH);
  delayMicroseconds(10); 
 
  digitalWrite(trigPin1, LOW);
  duration = pulseIn(echoPin1, HIGH);
 
  //Calculate the distance (in cm) based on the speed of sound.
  distance1 = duration/58.2;
  if(distance1==0)
  {
    distance1=1000;
    delay(200);
  }
  //Serial.print("D1 : ");Serial.println(distance1);
  Serial.print("U");Serial.println(distance1);
  //lcd.setCursor(0,0);
  //lcd.print("D1:     ");
  //lcd.setCursor(3,0);
  //lcd.print(distance1);

  pinMode(echoPin1,OUTPUT);
  digitalWrite(echoPin1,LOW);
  delay(50);
  pinMode(echoPin1,INPUT);
  delay(50);
}
//*************************************************
void ultrasonic_init()
{
  delay(100);
  pinMode(trigPin1, OUTPUT);
  pinMode(echoPin1, INPUT);
}
//*************************************************
void serialFlush(){
  while(Serial.available() > 0) {
    char t = Serial.read();
  }
} 
//*************************************************
void setup() {                
  // initialize the digital pin as an output.
  Serial.begin(9600);
  //lcd.begin(16, 2);
  // Print a message to the LCD.
  //lcd.print("MOBILE CONTROL. ");
  //lcd.setCursor(0, 1);    
  //lcd.print("     ROBOT      ");
  
  delay(1000);
  //lcd.clear();  
  
  pinMode(M0, OUTPUT);  pinMode(M2, OUTPUT);
  pinMode(M1, OUTPUT);  pinMode(M3, OUTPUT);

  digitalWrite(M0, LOW);   digitalWrite(M1, LOW);   
  digitalWrite(M2, LOW);   digitalWrite(M3, LOW);
  ultrasonic_init();
  previousMillis = millis();
}  
//*************************************************
// the loop routine runs over and over again forever:
void loop() 
{
    if (Serial.available() > 0) 
    {
      incomingByte = Serial.read();
    }
    
    //lcd.setCursor(0, 0);    
    // say what you got:
    //Serial.print("I received: ");
    //Serial.println(incomingByte);
 
    if(incomingByte == 'F')    // FORWARD
    {
      //lcd.setCursor(0, 1);
      //lcd.print("    FORWARD     ");
      digitalWrite(M0, LOW);    digitalWrite(M1, HIGH);   
      digitalWrite(M2, HIGH);   digitalWrite(M3, LOW);
      serialFlush();
      fwd_flag=1;
    }
    else if(incomingByte == 'B')    // BACKWARD
    {
      //lcd.setCursor(0, 1);
      //lcd.print("   BACKWARD     ");
      digitalWrite(M0, HIGH);  digitalWrite(M1, LOW);   
      digitalWrite(M2, LOW);   digitalWrite(M3, HIGH);
      serialFlush();
      fwd_flag=0;
    }
    else if(incomingByte == 'L')   // LEFT
    {
      //lcd.setCursor(0, 1);
      //lcd.print("     LEFT       ");
      digitalWrite(M0, HIGH);    digitalWrite(M1, LOW);   
      digitalWrite(M2, HIGH);    digitalWrite(M3, LOW);
      serialFlush();
      fwd_flag=0;
    }
    else if(incomingByte == 'R')   // RIGHT
    {
      //lcd.setCursor(0, 1);
      //lcd.print("    RIGHT       ");
      digitalWrite(M0, LOW);  digitalWrite(M1, HIGH);   
      digitalWrite(M2, LOW);  digitalWrite(M3, HIGH);
      serialFlush();
      fwd_flag=0;
    }
    else if(incomingByte == 'S')
    {
      //lcd.setCursor(0, 1);
      //lcd.print("     STOP       ");
      digitalWrite(M0, LOW);  digitalWrite(M1, LOW);   
      digitalWrite(M2, LOW);  digitalWrite(M3, LOW);
      serialFlush();
      fwd_flag=0;  
    }    

    if(fwd_flag==1)
    {
      if(distance1<30)
      {
        digitalWrite(M0, LOW);  digitalWrite(M1, LOW);   
        digitalWrite(M2, LOW);  digitalWrite(M3, LOW);
        delay(2000);
      }
    }
    
    if(millis() - previousMillis > 1000)
    {
      read1_hcsr04();
      previousMillis = millis();

    }
    delay(100);  
}
//*************************************************
