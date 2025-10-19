# Integrity Hash (Java Swing Frontend)

 <a href="https://github.com/pwssOrg/File-Integrity-Scanner">
<img width="1917" height="540" alt="pic3" src="https://github.com/pwssOrg/File-Integrity-GUI/blob/develop/.github/assets/images/pic3.png" />
 </a>

[![Build FIS-GUI](https://github.com/pwssOrg/File-Integrity-GUI/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/pwssOrg/File-Integrity-GUI/actions/workflows/build.yml)
[![SCA Scan - File Integrity GUI](https://github.com/pwssOrg/File-Integrity-GUI/actions/workflows/snyk-scan.yml/badge.svg)](https://github.com/pwssOrg/File-Integrity-GUI/actions/workflows/snyk-scan.yml)


  

Integrity Hash is a secure, user-friendly Java Swing desktop client for managing and visualizing file-integrity scans. It connects locally to the File-Integrity Scanner backend to monitor scans, verify file hashes, and review historical results — all without external network dependencies.






## What is the File-Integrity Scanner Frontend?

The Swing-based frontend is a **desktop companion application** that allows users to interact with the File-Integrity Scanner backend in real time. It simplifies complex operations such as initiating scans, verifying file hashes, and viewing scan history, all through a clean, responsive, and local user interface.

## Key Features

- **Verify integrity of your files:**  
    Monitor file integrity status, view scan results and detect anomalies.

 
  <img width="1586" height="793" alt="scan_in_progress" src="https://github.com/pwssOrg/File-Integrity-GUI/blob/ignoreFile_Fix/.github/assets/images/scan_in_progress.png" />



- **Detailed File View & History:**  
    Inspect metadata, compare hash snapshots, and review scan timelines with timestamps.
  <img width="1586" height="793" alt="fileSearch" src="https://github.com/pwssOrg/File-Integrity-GUI/blob/ignoreFile_Fix/.github/assets/images/fileSearch.png" />


- **Hash Algorithm Visualization:**  
    Supports displaying results for SHA-256, SHA-3 (256-bit), and BLAKE2b (512-bit).
  <img width="1586" height="793" alt="scan_results" src="https://github.com/pwssOrg/File-Integrity-GUI/blob/ignoreFile_Fix/.github/assets/images/scan_results.png" />


- **Secure Local Operation:**  
    All operations and communications occur locally — **no external network services or cloud dependencies**.

  <img width="386" height="193" alt="Integrity Hash Login Screen" src="https://github.com/pwssOrg/File-Integrity-GUI/blob/develop/.github/assets/images/login2.png" />


- **Lightweight Swing UI:**  
    Built with pure Java Swing for performance and reliability — platform‑independent across Windows, macOS, and Linux.
   <img width="776" height="392" alt="Home Screen" src="https://github.com/pwssOrg/File-Integrity-GUI/blob/develop/.github/assets/images/pic2.png" />
## Requirements

- **Java 21+**  
- **Maven 4.0.0+**  
- **Local Backend Server**: Ensure the local backend server is running to use the Integrity Hash GUI App.
- **License**: We kindly ask you to purchase one from PWSS Org.

## Security 

To use SSL with the local backend server, you will need a `truststore.jks` file that contains the public key for
the local backend server.

## Diff 
<img width="378" height="151" alt="diff1" src="https://github.com/pwssOrg/File-Integrity-GUI/blob/ignoreFile_Fix/.github/assets/images/diff1.png" />

Receive notifications about file diffs and observe the distinction and original hashes from all three hash algorithms. The PostgreSQL database secures your file integrity history and cannot be deleted unless your database account is compromised, which is great.  For organizations that like to backtrack file history or for authorities who have hacked someone's computer and require all the evidence and file changes to conduct a quick investigation, this feature is invaluable.

<img width="1586" height="793" alt="diff_detected" src="https://github.com/pwssOrg/File-Integrity-GUI/blob/ignoreFile_Fix/.github/assets/images/diff_detected.png" />

### Shady file? :lock:

When a file that shouldn't be changed suddenly appears in a diff and you don't want to take any chances - Quarantine the file instantly from the app!   Once the quarantine operation is finished, the file will no longer be able to cause any more damage.  Have you made a mistake and would like to retrieve your quarantined file? No worries, just unquench the file and it will end up in the same location as before in the same state. 

Please be careful when quarantining files that are system files.  A quarantined file won't work if it's quarantined, so my best suggestion to you is to avoid the Windows folder. Anti-virus products and Windows Defender (if you're using Integrity Hash on Windows) will handle that.  Integrity hash is a product that safeguards the files that are important to you or your organization. No more undetectable file injections or undetectable SCI FI quantum attack. Even if you have full access to a computer's kernel. Try to screw up the OS so much that all three hash algorithms become compromised. There are only a handful of individuals in the world who can accomplish that with just one algorithm (like SHA-256).  In order to prevent a diff result, it is necessary for all three hash algorithms to match.

## How to set up for PWSS Clients?



PWSS clients will have a trust store bundled with the Integrity Hash application and can easily use our install script (Windows and Linux) for both the application (FE BE) and the Postgresql database underneath. SSL can be used in your local environment without any additional setup. No local wireshark sniffing for your credentials, in other words. :closed_lock_with_key:

Your machine will have the local server certificate, but what happens if an attacker identifies it after they got foothold of your environment? They would need the password and then launch a noisy man in the middle tool. In many enterprise environments, it won't be feasible.  You have control over your database password and username, and we won't be aware of it even if you use our setup script. The setup process for this local application will be straightforward for non-developers.   Understanding the value of this is important if you care about security.

## Contact Information

For any questions or support, please reach out to:

- @pwgit-create — Peter (pwgit-create)
- @lilstiffy — Stefan (lilstiffy)



### Discussion Forum


:arrow_down:

 <a href="https://github.com/orgs/pwssOrg/discussions/categories/file-integrity-gui">
    <img src="https://github.com/pwssOrg/File-Integrity-GUI/blob/develop/.github/assets/images/640x486.jpg"
         alt="purple-and-blue-light-digital-wallpaper"  width="128" height="96"/>
  </a>


