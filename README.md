# OneTwoThreeSDK for Android

![Minimum SDK Version](https://img.shields.io/badge/minSdkVersion-23-brightgreen)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.2c2p/onetwothreesdk/badge.svg)](https://search.maven.org/artifact/com.2c2p/onetwothreesdk)

## Installation

Download the latest AAR from Maven Central or grab via Gradle:

```kotlin
implementation 'com.2c2p:onetwothreesdk:1.0.0'
```


or Maven:

```xml
<dependency>
    <groupId>com.2c2p</groupId>
    <artifactId>onetwothreesdk</artifactId>
    <version>1.0.0</version>
    <type>aar</type>
</dependency>
```

## Usage

Setup using the following snippet, e.g. in your MyApplication.kt onCreate method:

```kotlin
OneTwoThreeSDKService.initialize(
            isProduction = false, // Use in production or not
            checkSumKey = "", // Checksum key
            publicKey = "", // Public key
            privateKey = "", // Private key
            passphrase = "", // Passphrase for private key
            bksPassphrase = "" // BKS passphrase,
        )
```

### Sample

```kotlin

merchant = Merchant(
    id = "",
    redirectURL = "",
    notificationURL = "",
    merchantData = listOf(
        MerchantData(item = ""),
        MerchantData(item = ""),
        MerchantData(item = ""),
        MerchantData(item = "")
    )
)

transaction = Transaction(
    merchantReference = UUID.randomUUID().toString().replace("-", "").substring(0,11),
    preferredAgent = "",
    productDesc = "Description of the product.",
    amount = "1.00",
    currencyCode = "",
    paymentInfo = "",
    paymentExpiry = ""
)

buyer = Buyer(
    email = "",
    mobile = "",
    language = "",
    notifyBuyer = true,
    title = "",
    firstName = "",
    lastName = ""
)

OneTwoThreeSDKService.startDeeplink(merchant, transaction, buyer, object :
    APIResponseCallback<StartDeeplinkResponse> {
    override fun onResponse(response: StartDeeplinkResponse) {
        // TODO: Success

        try {
            // NOTE: Redirect to bank app using 'deeplinkURL' from the response.
            val launcher = Intent(Intent.ACTION_VIEW)
            launcher.data = Uri.parse(response.deeplinkURL)
            startActivity(launcher)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFailure(error: Throwable?) {
        // TODO: Failed
    }
})
```

## Contributing
2C2P

## License
[MIT](https://choosealicense.com/licenses/mit/)