import sys
from PyQt5 import QtWidgets, QtCore
from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import Qt

 
class App(QWidget):
 
    def initUI(self, Mainwindow):

        MainWindow.setObjectName("Hmmteresting...")
        MainWindow.resize(800, 600)
        MainWindow.setAutoFillBackground(True)
        p = MainWindow.palette()
        p.setColor(self.backgroundRole(), Qt.black)
        MainWindow.setPalette(p)
    
    def scheduleUI(self, MainWindow):
        MainWindow.setObjectName("Hmmteresting...")
        MainWindow.resize(800, 600)
        MainWindow.setAutoFillBackground(True)
        p = MainWindow.palette()
        p.setColor(self.backgroundRole(), Qt.black)
        MainWindow.setPalette(p)
        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.Date = QtWidgets.QLabel(self.centralwidget)
        self.Date.setGeometry(QtCore.QRect(30, 30, 141, 31))
        self.Date.setTextFormat(QtCore.Qt.PlainText)
        self.Date.setObjectName("Date")
        self.Time = QtWidgets.QLabel(self.centralwidget)
        self.Time.setGeometry(QtCore.QRect(30, 70, 141, 31))
        self.Time.setTextFormat(QtCore.Qt.PlainText)
        self.Time.setObjectName("Time")
        self.Schedule = QtWidgets.QLabel(self.centralwidget)
        self.Schedule.setGeometry(QtCore.QRect(30, 120, 171, 211))
        self.Schedule.setObjectName("Schedule")
        self.Name = QtWidgets.QLabel(self.centralwidget)
        self.Name.setGeometry(QtCore.QRect(230, 30, 301, 51))
        self.Name.setObjectName("Name")
        MainWindow.setCentralWidget(self.centralwidget)

        MainWindow.setWindowTitle("Hmmteresting")
        self.Date.setText("Date")
        self.Date.setStyleSheet('color : white')
        self.Time.setText("Time")
        self.Time.setStyleSheet('color : white')
        self.Schedule.setText("Schedule")
        self.Schedule.setStyleSheet('color : white')
        self.Name.setText("Name")
    
    def closeUI(self, MainWindow):
        return 0


if __name__ == '__main__':

    app = QtWidgets.QApplication(sys.argv)
    MainWindow = QtWidgets.QMainWindow()
    ui = App()
    ui.scheduleUI(MainWindow)
    #ui.initUI(MainWindow)
    MainWindow.show()
    sys.exit(app.exec_())
