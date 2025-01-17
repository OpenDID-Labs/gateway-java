**OpenDID** is an infrastructure that connects all major cryptography-based digital identity and decentralized identifier (DID) systems, routing encrypted messages between them and business systems. It enables developers and applications to easily register, authenticate, and verify identities and verifiable credentials (VCs) across diverse platforms.

As a messaging backbone similar to SWIFT, but for identity systems, OpenDID provides two types of Access Points: Web3-based Oracles deployed on various public chains and Web2-based Gateways deployed locally. Both access methods are open-source at GitHub and can be freely accessed or installed. For detailed information about the OpenDID Project, please read the whitepaper below.

Web2 Gateway is centralized software that users can install in their local environments to interact with OpenDID through Web2 protocols. Currently, we provide a Java version of Web2 Gateway, When you send a verification request to OpenDID through `opendid-gateway-java` , you need to pay for the APT on the APTOS mainnet. Therefore, you need to prepare an APTOS wallet and recharge enough APT on the mainnet to ensure that OpenDID can handle your verification request normally.

See the  [Deployment Manual](https://github.com/OpenDID-Labs/gateway-java/blob/main/Deployment%20Manual.md) for running your own gateway in a production environment.

