import speech_recognition as sr


class SpeechToText:
    def __init__(self):
        self.mic = sr.Recognizer()
    def listen(self):
        with sr.Microphone() as source:
            self.mic.adjust_for_ambient_noise(source)

            print("Please say something")

            audio = self.mic.listen(source)

            print("Recognizing Now .... ")

            try:
                print("You have said \n" + self.mic.recognize_google(audio))
                print("Audio Recorded Successfully \n ")
            except Exception as e:
                print("Error :  " + str(e))

if __name__ == "__main__":
    mic = SpeechToText()
    mic.listen()
