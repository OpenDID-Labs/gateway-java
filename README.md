**OpenDID** is an infrastructure that connects all major asymmetric encryption-based digital identity and decentralized identifier systems, routing encrypted messages between them and enabling developers and business applications to easily register, authenticate, and verify identities and verifiable credentials across diverse platforms. OpenDID now integrates 10 additional identity systems: China RealDID, Terminal3 Identity, ENS, Farcaster ID, HashKey DID, Passport XYZ, Privado ID, World ID, cheqd DID, Lens.

As a messaging backbone similar to the SWIFT network for identity systems, OpenDID provides two types of Access Points: Web3-based Oracles and Web2-based Gateways. Both access methods are open-source at GitHub and available for public audits.



Currently, we provide a Java version of Web2 Gateway， When you send a verification request to OpenDID through `opendid-gateway-java` , you need to pay for the APT on the APTOS mainnet. Therefore, you need to prepare an APTOS wallet and recharge enough APT on the mainnet to ensure that OpenDID can handle your verification request normally.

See the  [Deployment Manual](https://github.com/OpenDID-Labs/gateway-java/blob/main/Deployment%20Manual.md) for running your own gateway in a production environment.

