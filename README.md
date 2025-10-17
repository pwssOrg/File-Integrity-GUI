# Integrity Hash (Java Swing Frontend)

## Overview

Integrity Hash is a secure, user-friendly Java Swing desktop client for managing and visualizing file-integrity scans. It connects locally to the File-Integrity Scanner backend to monitor scans, verify file hashes, and review historical results — all without external network dependencies.

## What is the File-Integrity Scanner Frontend?

The Swing-based frontend is a **desktop companion application** that allows users to interact with the File-Integrity Scanner backend in real time. It simplifies complex operations such as initiating scans, verifying file hashes, and viewing scan history, all through a clean, responsive, and local user interface.

## Key Features

- **Verify integrity of your files:**  
    Monitor file integrity status, view scan results and detect anomalies.

- **Detailed File View & History:**  
    Inspect metadata, compare hash snapshots, and review scan timelines with timestamps.

- **Hash Algorithm Visualization:**  
    Supports displaying results for SHA-256, SHA-3 (256-bit), and BLAKE_2b (512-bit).

- **Secure Local Operation:**  
    All operations and communications occur locally — **no external network services or cloud dependencies**.

- **Lightweight Swing UI:**  
    Built with pure Java Swing for performance and reliability — platform‑independent across Windows, macOS, and Linux.


![Integrity Hash Image](https://github.com/pwssOrg/File-Integrity-GUI/blob/master/.github/assets/images/640x486.jpg?raw=true)

---

### Requirements

- **Java 21+**  
- **Maven 4.0.0+**  
- **Backend File-Integrity Scanner (Spring Application)** running locally  

## Workflow Badges

[![Build FIS-GUI](https://github.com/pwssOrg/File-Integrity-GUI/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/pwssOrg/File-Integrity-GUI/actions/workflows/build.yml)
[![SCA Scan - File Integrity GUI](https://github.com/pwssOrg/File-Integrity-GUI/actions/workflows/snyk-scan.yml/badge.svg)](https://github.com/pwssOrg/File-Integrity-GUI/actions/workflows/snyk-scan.yml)

## Contact Information

For any questions or support, please reach out to:

	@pwgit-create	Peter pwgit-create
	@lilstiffy	Stefan lilstiffy

### Discussion Forum

Please visit our discussion forum for project-related documentation and discussions: [Project Discussion
Forum](https://github.com/orgs/pwssOrg/discussions/categories/file-integrity-gui)