#!/usr/bin/env python
# coding=utf8
import sys
import json

from pprint import pprint
from PyQt5 import QtWidgets, QtCore, QtGui
from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import Qt
from PyQt5.QtWidgets import QApplication, QWidget, QLabel
from PyQt5.QtGui import QIcon, QPixmap
import sys

reload(sys)
sys.setdefaultencoding('utf-8')

class App(QWidget):
    
    with open('test.json') as data_file:
        data = json.load(data_file)
    
    def initUI(self, Mainwindow):

        MainWindow.setObjectName("Hmmteresting...")
        MainWindow.resize(1920, 1080)
        MainWindow.setAutoFillBackground(True)
        p = MainWindow.palette()
        p.setColor(self.backgroundRole(), Qt.black)
        MainWindow.setPalette(p)
    
    def scheduleUI(self, MainWindow):

        pixmap_weather = QPixmap('/home/kyungwook/kyungwook/weather_img/cloud.PNG')
        pixmap_message = QPixmap('/home/kyungwook/kyungwook/weather_img/message.PNG')

        MainWindow.setObjectName("Hmmteresting...")
        MainWindow.resize(1920, 1080)
        MainWindow.setAutoFillBackground(True)
        p = MainWindow.palette()
        p.setColor(self.backgroundRole(), Qt.black)
        MainWindow.setPalette(p)
        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.UserName = QtWidgets.QLabel(self.centralwidget)
        self.UserName.setGeometry(QtCore.QRect(25, 10, 800, 150))
        self.UserName.setTextFormat(QtCore.Qt.PlainText)
        self.UserName.setObjectName("UserName")
        self.Date = QtWidgets.QLabel(self.centralwidget)
        self.Date.setGeometry(QtCore.QRect(1200, 30, 690, 100))
        self.Date.setTextFormat(QtCore.Qt.PlainText)
        self.Date.setObjectName("Date")
        self.Time = QtWidgets.QLabel(self.centralwidget)
        self.Time.setGeometry(QtCore.QRect(1200, 140, 690, 100))
        self.Time.setTextFormat(QtCore.Qt.PlainText)
        self.Time.setObjectName("Time")
        self.Message_image = QtWidgets.QLabel(self.centralwidget)

        self.Weather_image = QtWidgets.QLabel(self.centralwidget)
        self.Weather_image.setGeometry(QtCore.QRect(1490, 230, 120, 120))
        self.Weather_image.setPixmap(pixmap_weather)
        self.Weather_image.setObjectName("Weather_image") 
        self.Temperature = QtWidgets.QLabel(self.centralwidget)
        self.Temperature.setGeometry(QtCore.QRect(1570, 230, 310, 120))
        self.Temperature.setObjectName("Temperature")

        self.Schedule = QtWidgets.QLabel(self.centralwidget)
        self.Schedule.setGeometry(QtCore.QRect(30, 180, 800, 300))
        self.Schedule.setObjectName("Schedule")
        self.Materaials_name = QtWidgets.QLabel(self.centralwidget)
        self.Materaials_name.setGeometry(QtCore.QRect(30, 800, 800, 40))
        self.Materaials_name.setObjectName("Materaials_name")
        self.Materials = QtWidgets.QLabel(self.centralwidget)
        self.Materials.setGeometry(QtCore.QRect(30, 850, 800, 50))
        self.Materials.setObjectName("Materials")

        ########################## 영상 메세지 들어 올 시에 이미지 ###############################
        self.Message_image.setGeometry(QtCore.QRect(1780, 850, 100, 100))
        self.Message_image.setPixmap(pixmap_message)
        self.Message_image.setObjectName("Message_image")
        self.Message_number = QtWidgets.QLabel(self.centralwidget)
        self.Message_number.setGeometry(QtCore.QRect(1860, 860, 30, 30))
        self.Message_number.setObjectName("Message_number")
        ########################################################################################
      
        MainWindow.setCentralWidget(self.centralwidget)
        MainWindow.setWindowTitle("Hmmteresting")

        date_text = self.data["Date"]
        time_text = self.data["Time"]
        user_id = self.data["Name"]
        say_hello = "안녕! " + user_id
        temperature_num = "18.5"
        temparature_index = temperature_num + "˚C"
        schedule_text = "9:00 ~ 11:00 Ajou Greative Contest\n11:00 ~ 12:00 : 점심시간 \n13:00 ~ 15:00 : 시상식"
        materials_text = "노트북, 공유기"
        message_number = "1"

        self.UserName.setText(say_hello)
        self.UserName.setFont(QtGui.QFont('SansSerif', 70))
        self.UserName.setStyleSheet('color : white')
        self.Date.setText(date_text)
        self.Date.setAlignment(Qt.AlignRight)
        self.Date.setFont(QtGui.QFont('SansSerif', 50))
        self.Date.setStyleSheet('color : white')
        self.Time.setText(time_text)
        self.Time.setAlignment(Qt.AlignRight)
        self.Time.setFont(QtGui.QFont('SansSerif', 40))
        self.Time.setStyleSheet('color : white')
        self.Schedule.setText(schedule_text)
        self.Schedule.setFont(QtGui.QFont('SansSerif', 20))
        self.Schedule.setStyleSheet('color : white')
        self.Materaials_name.setText("☆ 챙겨야 할 것 ☆")
        self.Materaials_name.setFont(QtGui.QFont('SansSerif', 25))
        self.Materaials_name.setStyleSheet('color : white')
        self.Materials.setText(materials_text)
        self.Materials.setFont(QtGui.QFont('SansSerif', 20))
        self.Materials.setStyleSheet('color : white')
        self.Temperature.setText(temparature_index)
        self.Temperature.setFont(QtGui.QFont('SansSerif', 60))
        self.Temperature.setStyleSheet('color : white')
        self.Temperature.setAlignment(Qt.AlignRight)

        ################################# 메세지 개수 ##########################
        self.Message_number.setText(message_number)
        self.Message_number.setFont(QtGui.QFont('SansSerif', 15))
        self.Message_number.setStyleSheet('color : red')
        self.Message_number.setAlignment(Qt.AlignRight)


    
    def closeUI(self, MainWindow):
        QApplication.quit()


if __name__ == '__main__':

    app = QtWidgets.QApplication(sys.argv)
    MainWindow = QtWidgets.QMainWindow()
    ui = App()
    ui.scheduleUI(MainWindow)
    #ui.initUI(MainWindow)
    MainWindow.show()
    app.exec_()
