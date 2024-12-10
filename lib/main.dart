// ignore_for_file: library_private_types_in_public_api

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: PhoneExtractorScreen(),
    );
  }
}

class PhoneExtractorScreen extends StatefulWidget {
  const PhoneExtractorScreen({super.key});

  @override
  _PhoneExtractorScreenState createState() => _PhoneExtractorScreenState();
}

class _PhoneExtractorScreenState extends State<PhoneExtractorScreen> {
  late FlutterLocalNotificationsPlugin flutterLocalNotificationsPlugin;

  @override
  void initState() {
    super.initState();
    flutterLocalNotificationsPlugin = FlutterLocalNotificationsPlugin();
    const androidSettings =
        AndroidInitializationSettings('@mipmap/ic_launcher');
    const initSettings = InitializationSettings(android: androidSettings);

    flutterLocalNotificationsPlugin.initialize(initSettings).then((value) {
      if (kDebugMode) {
        print("Notification plugin initialized");
      }
      _showNotification();
    }).catchError((e) {
      if (kDebugMode) {
        print("Error initializing notifications: $e");
      }
    });
  }

  Future<void> _showNotification() async {
    const androidDetails = AndroidNotificationDetails(
      'channelId',
      'Phone Extractor',
      channelDescription: 'Service to extract phone numbers',
      importance: Importance.max,
      priority: Priority.high,
      ongoing: true,
    );
    const notificationDetails = NotificationDetails(android: androidDetails);

    try {
      await flutterLocalNotificationsPlugin.show(
        0,
        'Phone Extractor Running',
        'Tap to start extracting phone numbers',
        notificationDetails,
      );
      if (kDebugMode) {
        print("Notification shown");
      }
    } catch (e) {
      if (kDebugMode) {
        print("Error showing notification: $e");
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Phone Extractor')),
      body: const Center(child: Text('Service is running...')),
    );
  }
}
