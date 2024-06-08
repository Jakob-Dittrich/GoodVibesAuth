# GoodVibesAuth: Mobile Device-to-User Authentication via Wristwatch Vibrations

### Overview
GoodVibesAuth is an Android application paired with a smartwatch companion app designed to enhance mobile security through device-to-user authentication. Utilizing vibration patterns, GoodVibesAuth allows users to confirm the authenticity of their mobile devices directly through their wristwatches. This method aims to prevent unauthorized access and phishing attacks, improving user awareness and security.
Additionally, GoodVibesAuth can be controlled remotely through a hosted web server. The phone app displays the server's IP address, and specific routes allow you to manage the vibrations. Detailed descriptions of the specific routes are provided below.

### Features
- **Secure Authentication**: Uses pre-selected vibration patterns to verify device authenticity.
- **High Accuracy**: Users can recognize correct authentication vibrations with 98% success.
- **Multiple Vibration Patterns**: Choose from predefined patterns for personalized security.
- **Real-time Notifications**: Instant feedback on wristwatch when interacting with your mobile device.

### How It Works
1. **Setup**: Install the GoodVibesAuth app on both your Android phone and smartwatch.
2. **Configuration**: Pair your devices via Bluetooth and select your personal authentication vibration pattern. In the phones app you can choose your vibration pattern.
3. **Test Vibration**: Via the phones app you can send a vibraiton with the middle button, or over the hosted website.
4. **Authentication**: When you wake your mobile device, it sends a notification to your smartwatch, triggering the selected vibration pattern. This confirms you are interacting with the correct device.

### Installation

#### Requirements
- Android phone (version 9.X or higher)
- Compatible smartwatch (version 9.X or higher)
- Bluetooth enabled on both devices

#### Steps
1. Clone the repository:
    ```bash
    git clone https://github.com/Jakob-Dittrich/GoodVibesAuth
    ```
2. Navigate to the project directory:
    ```bash
    cd GoodVibesAuth
    ```
3. Install dependencies:
    ```bash
    npm install
    ```
4. Install the app on both your phone and smartwatch:
    ```bash
    # Follow the provided instructions for installing Android apps.
    ```

### Usage
1. **Start the App**: Open the GoodVibesAuth app on both your phone and smartwatch.
2. **Wake Your Phone**: Trigger the phone's screen to wake up.
3. **Verify Vibration**: Feel the vibration pattern on your smartwatch to confirm device authenticity.

#### Control the App/Vibrations
Web Server Control Routes:

    /turnOn: Activate vibrations.
    /turnOff: Deactivate vibrations.
    /vibrate: Trigger the watch to vibrate in the selected authentication pattern.
    /vibrate_rand: Trigger the watch to vibrate in a random pattern.
    /vibrate_wrong: Set the authentication vibration to a random pattern, then activate or deactivate as needed.

Access these controls by navigating to http://<phone-ip>/<route> in your web browser, where <phone-ip> is the IP address displayed in the phone app. This feature allows flexible and convenient management of your device-to-user authentication system.

### Evaluation
In a controlled study with 30 participants, GoodVibesAuth demonstrated:
- 98% success rate in recognizing correct authentication vibrations.
- Over 90% accuracy in identifying incorrect or missing vibrations.
- High usability and user satisfaction, with participants finding the system easy to use and adapt to.

### License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

### Acknowledgments
- Developed by Jakob Dittrich and Rainhard Dieter Findling.
- Supported by SAIL Department, University of Applied Sciences Upper Austria, and Google LLC.
- Inspired by research on device-to-user authentication to enhance mobile security.

For more details, refer to the research paper.

---

This description provides a comprehensive overview of the GoodVibesAuth project, covering its purpose, features, setup, usage, evaluation results.