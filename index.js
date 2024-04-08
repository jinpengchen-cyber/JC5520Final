const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendFollowNotification = functions.firestore
    .document('users/{userId}/followers/{followerId}')
    .onCreate((snapshot, context) => {
        const userId = context.params.userId;
        const followerId = context.params.followerId;

        // Here you would get the FCM token of the user being followed and send them a notification
        const userRef = admin.firestore().collection('users').doc(userId);

        return userRef.get().then(userDoc => {
            const fcmToken = userDoc.data().fcmToken;

            const message = {
                notification: {
                    title: 'New Follower',
                    body: `${followerId} is now following you!`,
                },
                token: fcmToken,
            };

            return admin.messaging().send(message);
        }).catch(error => {
            console.error("Error sending message:", error);
        });
    });

