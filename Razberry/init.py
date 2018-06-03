#!/usr/bin/env python
# coding=utf8
import sys
from PyQt5 import QtWidgets, QtCore, QtGui
from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import Qt
from PyQt5.QtWidgets import QApplication, QWidget, QLabel
from PyQt5.QtGui import QIcon, QPixmap
 
class App(QWidget):
 
    def initUI(self, Mainwindow):

        MainWindow.setObjectName("Hmmteresting...")
        MainWindow.resize(1920, 1080)
        MainWindow.setAutoFillBackground(True)
        p = MainWindow.palette()
        p.setColor(self.backgroundRole(), Qt.black)
        MainWindow.setPalette(p)
       
    def closeUI(self, MainWindow):
        QApplication.quit()


if __name__ == '__main__':

    app = QtWidgets.QApplication(sys.argv)
    MainWindow = QtWidgets.QMainWindow()
    ui = App()
    ui.initUI(MainWindow)
    MainWindow.showFullScreen()
    app.exec_()
