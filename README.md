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


