# Gateway-java

<img width="300" alt="OpenDID Logo" src="https://github.com/45be6ca9-cab2-4a90-b113-2ea0daad04dc">

## Document

Website: https://www.opendid.io/

Documentation: [OpenDID Reference](https://opendid.readme.io/reference/getting-started-1)

Tutorials: [gateway-deploy-java](https://github.com/OpenDID-Labs/gateway-deploy-java)


##  Configuration Description

common.yaml Property

| Property	                    | Description                                  |
|-----------------------|-------------------------------------|
| local.privatekey      | Private key of the key pair used for secure authentication with VN, based on the secp256k1 algorithm.    |
| local.publickey       | Public key of the key pair used for secure authentication with VN, based on the secp256k1 algorithm.|
| local.address         | Account address corresponding to local.publickey, based on the secp256k1 algorithm. |
| home-chain.privatekey | Private key of the key pair used for sending transactions to the home chain, based on the Ed25519 algorithm.        |
| home-chain.publickey  | Public key of the key pair used for sending transactions to the home chain, based on the Ed25519 algorithm.        |
| oracle.callBack.url   | Callback URL for VN to notify the transaction results.                      |

## Configuration Generation Instructions
The five properties local.privatekey, local.publickey, local.address, home-chain.privatekey, and home-chain.publickey can be generated using the provided SDK.

SDK URL:
[gateway-keytoolkit-java](https://github.com/OpenDID-Labs/gateway-keytoolkit-java)

For detailed usage instructions, refer to the README.md file in the SDK project.