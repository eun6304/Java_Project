<?php

$directory = "./uploads/";
$handle = opendir($directory); 
while ($file = readdir($handle)) {
        @unlink($directory.$file);
}
closedir($handle);
?>