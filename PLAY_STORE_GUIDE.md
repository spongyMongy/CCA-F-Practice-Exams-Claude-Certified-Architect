# Step-by-Step Play Store Upload Guide

## App Information
*   **Official App Name**: Architect Elite: Claude Exam
*   **Package Name**: `com.arslan.ccafprep`

---

## Step 1: Generate the Signed Release Bundle (AAB)
1.  In Android Studio, go to **Build** > **Generate Signed Bundle / APK...**
2.  Select **Android App Bundle** and click **Next**.
3.  **Key Store Path**: Select your `upload-keystore.jks` file.
4.  Enter your passwords and Key Alias.
5.  Select **release** Build Variant.
6.  Click **Create**. The `.aab` file will be generated in `app/release/`.

## Step 2: Create App in Google Play Console
1.  Go to [Google Play Console](https://play.google.com/console).
2.  Click **Create app**.
3.  Enter **App Name**: `Architect Elite: Claude Exam`
4.  Default language: **English**.
5.  App or Game: **App**.
6.  Free or Paid: **Free** (The $4.99 is an In-App Purchase, not an upfront price).

## Step 3: Set Up Your App
Complete the "Initial Setup" tasks on the dashboard:
1.  **App Access**: Select "All functionality is available without special access".
2.  **Ads**: Select "No".
3.  **Content Rating**: Complete the questionnaire (Educational category).
4.  **Target Audience**: Select **18 and over** (Architect level content).
5.  **Data Safety**:
    *   Does your app collect or share data? **No**.
    *   (Billing SDK uses device IDs, but we don't collect personal user data).

## Step 4: Configure In-App Purchase (Billing)
1.  Navigate to **Monetize** > **Products** > **In-app products**.
2.  Click **Create product**.
3.  **Product ID**: `full_unlock` (Must match the code in `BillingManager.kt`).
4.  Title: **Full Architect Unlock**.
5.  Price: Set to **$4.99**.
6.  Save and **Activate**.

## Step 5: Upload the Bundle
1.  Go to **Testing** > **Production**.
2.  Create a **new release**.
3.  Upload the `.aab` file generated in Step 1.
4.  Enter **Release Name**: `1.0.0 (Initial Release)`.
5.  Copy the **Release Notes** from the Full Description we drafted.
6.  Click **Save**, then **Review release**, and finally **Start rollout to Production**.

---
> [!IMPORTANT]
> Google review typically takes **1–3 business days** for first-time apps. Ensure your screenshots look high-quality to avoid rejection!
