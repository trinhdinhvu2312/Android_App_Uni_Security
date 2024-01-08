//create keystore
keytool -genkeypair -alias springboot -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore springboot.p12 -validity 3650 -storepass password
//view list keystore
keytool -list -v -keystore springboot.p12
//
keytool -import -alias springboot -file myCertificate.crt -keystore springboot.p12 -storepass password