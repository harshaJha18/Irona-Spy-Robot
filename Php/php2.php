<?php
error_reporting(E_ALL);
ini_set('display_errors', '1');
include "php_serial_class.php";

// receive data from app's http request
$data=$_POST["action"];
//$data = "[FWD]";
$serial = new phpSerial;
$serial->deviceSet("/dev/ttyAMA0");
$serial->confBaudRate(9600);
$serial->confParity("none");
$serial->confCharacterLength(8);
$serial->confStopBits(1);
$serial->deviceOpen();

//Send data to Arduino
//$serial->sendMessage($data);
if($data=="[FWD]")
{
	$serial->sendMessage("F");
	echo "[F-OK]";
}
else if($data=="[BWD]")
{
	$serial->sendMessage("B");
	echo "[B-OK]";
}
else if($data=="[LFT]")
{
	$serial->sendMessage("L");
	echo "[L-OK]";
}
else if($data=="[RGT]")
{
	$serial->sendMessage("R");
	echo "[R-OK]";
}
else if($data=="[STP]")
{
	$serial->sendMessage("S");
	echo "[S-OK]";
}
else if($data=="[DIST]")
{
	$fp = @fopen("/home/pi/Python_scripts/ultra.txt", "r");
	if($fp)
	{
		$ul = fgets($fp);
		echo $ul;
		echo "    \r\n";
		fclose($fp);
	}
	else
	{
		echo "file not found";
	}
}
$serial->deviceClose();

?>
