#include "stm8l15x.h"
#include "stm8l15x_conf.h"
#include "stm8l_discovery_lcd.h"
#include "discover_board.h"
#include "icc_measure.h"
#include "discover_functions.h"
#include <stdio.h>


#define PC_DDR	(*(volatile uint8_t *)0x500c)
#define PC_CR1	(*(volatile uint8_t *)0x500d)

#define CLK_DIVR	(*(volatile uint8_t *)0x50c0)
#define CLK_PCKENR1	(*(volatile uint8_t *)0x50c3)

#define USART1_SR	(*(volatile uint8_t *)0x5230)
#define USART1_DR	(*(volatile uint8_t *)0x5231)
#define USART1_BRR1	(*(volatile uint8_t *)0x5232)
#define USART1_BRR2	(*(volatile uint8_t *)0x5233)
#define USART1_CR2	(*(volatile uint8_t *)0x5235)
#define USART1_CR3	(*(volatile uint8_t *)0x5236)

#define USART_CR2_TEN1 (1 << 3)
#define USART_CR3_STOP2 (1 << 5)
#define USART_CR3_STOP1 (1 << 4)
#define USART_SR_TXE1 (1 << 7)

/* Machine status used by main for active function set by user button in interrupt handler */
//uint8_t state_machine;

/* LCD bar graph: used for display active function */
//extern uint8_t t_bar[2];

/* Auto_test activation flag: set by interrupt handler if user button is pressed few seconds */
//extern bool Auto_test;

/* Set in interrupt handler for indicate that user button is pressed */ 
//extern bool KeyPressed;

void UART1_SendByte(u8 data)
{
    USART_SendData8(USART1, data);
    while (USART_GetFlagStatus(USART1, USART_FLAG_TC) == RESET);
		while (USART_GetFlagStatus(USART1, USART_FLAG_TXE) == RESET);
		//USART_ClearFlag(USART1, USART_FLAG_TC);
}
void UART1_SendStr(u8 str[])
{	
		int i = 0;
    while(str[i] != '\0')
    {
        UART1_SendByte(str[i]); /* ?-?¡¤¦Ì¡Â¨®?¡¤¡é?¨ª¨°???¡Á?¡¤?o¡¥¨ºy */
				i++;
		}   
		while (USART_GetFlagStatus(USART1, USART_FLAG_TXE) == RESET);
}

void main(void)
{
	u8 str[] = "steam!!";
	int i = 0;
	GPIO_Init(GPIOE, GPIO_Pin_7, GPIO_Mode_Out_PP_High_Fast);
	GPIO_Init(GPIOD, GPIO_Pin_1, GPIO_Mode_Out_PP_High_Fast);
	GPIO_Init(GPIOD, GPIO_Pin_0, GPIO_Mode_Out_PP_High_Fast);
	GPIO_Init(GPIOE, GPIO_Pin_4, GPIO_Mode_Out_PP_High_Fast);
	GPIO_Init(GPIOE, GPIO_Pin_5, GPIO_Mode_Out_PP_High_Fast);
	//MOTOR A
	GPIO_ResetBits(GPIOD, GPIO_Pin_1);
	GPIO_ResetBits(GPIOD, GPIO_Pin_0);
	//MOTOR B
	GPIO_ResetBits(GPIOE, GPIO_Pin_4);
	GPIO_ResetBits(GPIOE, GPIO_Pin_5);
	GPIO_Init(GPIOC, GPIO_Pin_3, GPIO_Mode_Out_PP_High_Fast);//TXD
	GPIO_Init(GPIOC, GPIO_Pin_2, GPIO_Mode_In_PU_No_IT);//RXD
	CLK_PeripheralClockConfig(CLK_Peripheral_USART1, ENABLE);
	USART_DeInit(USART1);
	/*
     * USART1
     * baud rate = 9600
     * data length = 8
     * stop bit = 1
     * no parity
     * read & write
     */
		 //USART_ClockInit(USART1, USART_Clock_Disable, USART_CPOL_Low,USART_CPHA_1Edge,USART_LastBit_Disable);
	USART_Init(USART1, (u32)9600, USART_WordLength_8b, USART_StopBits_1, USART_Parity_No, USART_Mode_Tx|USART_Mode_Rx);
	USART_ClearITPendingBit(USART1, USART_IT_RXNE);
	USART_ClearITPendingBit(USART1, USART_IT_TC);
	/*
	typedef enum { USART_IT_TXE        = (uint16_t)0x0277, /*!< Transmit interrupt 
               USART_IT_TC         = (uint16_t)0x0266, /*!< Transmission Complete interrupt 
               USART_IT_RXNE       = (uint16_t)0x0255, /*!< Receive interrupt 
               USART_IT_IDLE       = (uint16_t)0x0244, /*!< IDLE line interrupt 
               USART_IT_OR         = (uint16_t)0x0235, /*!< Overrun Error interrupt 
               USART_IT_PE         = (uint16_t)0x0100, /*!< Parity Error interrupt 
               USART_IT_ERR        = (uint16_t)0x0500, /*!< Error interrupt 
               USART_IT_NF         = (uint16_t)0x0102, /*!< Noise Error interrupt 
               USART_IT_FE         = (uint16_t)0x0101  /*!< Frame Error interrupt 
             } USART_IT_TypeDef;
	*/
	//USART_ITConfig(USART1, USART_IT_TC, ENABLE);
	USART_ITConfig(USART1, USART_IT_RXNE, ENABLE);
	USART_Cmd(USART1, ENABLE);
	enableInterrupts();
	//GPIO_Init(GPIOE, GPIO_Pin_7, GPIO_Mode_Out_PP_High_Fast);
	for(;;)
	{
		//UART1_SendStr(str);
		delay_ms(100);
	}
}
