import pika
import json
import base64
import win32print
import win32api
from PIL import Image
from PyPDF2 import PdfReader

# CloudAMQP connection settings
cloudamqp_url = 'amqps://sdpwqges:GoybPFCeHCxMjtk0eqSeAhEykG335zGL@puffin.rmq2.cloudamqp.com/sdpwqges'

def print_pdf(pdf_path):
    printer_name = win32print.GetDefaultPrinter()
    print(printer_name)

       # Create a PDF reader object
    pdf_reader = PdfReader(pdf_path)

    # Print each page of the PDF
    for page_num in range(len(pdf_reader.pages)):
        page = pdf_reader.pages[page_num]
        page_content = page.extract_text()

        # Print the page content using the default printer
        win32api.ShellExecute(0, "print", pdf_path, None, ".", 0)

def callback(ch, method, properties, body):
    received_message = json.loads(body.decode())  # Decode and parse JSON message
    filename = "Consumed.pdf"
    file_content_base64 = received_message['data']
    file_content = base64.b64decode(file_content_base64)
    # Write the file content to a file
    with open(filename, 'wb') as file:
        file.write(file_content)
    
    print_pdf(filename)
    print(f"Received and saved file: {filename}")

def consume_messages():
    # Establish a connection to CloudAMQP
    params = pika.URLParameters(cloudamqp_url)
    connection = pika.BlockingConnection(params)
    
    # Create a channel
    channel = connection.channel()

    # Declare a queue to consume messages from
    queue_name = 'TestQueue'
    channel.queue_declare(queue=queue_name, durable=True)

    # Set up the callback to handle received messages
    channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)

    print("Waiting for messages. To exit press CTRL+C")
    channel.start_consuming()

if __name__ == '__main__':
    consume_messages()