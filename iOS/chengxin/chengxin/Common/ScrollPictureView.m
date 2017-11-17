//
//  ScrollPictureView.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ScrollPictureView.h"
#import "Global.h"

@implementation ScrollPictureView
@synthesize aryPicture, strAddPic;
-(id) initWithFrame:(CGRect)frame :(NSMutableArray*)pictures
{
    self = [super initWithFrame:frame];

    aryPicture = pictures;
    strAddPic = @"上传图片";
    
    [self reloadPictureData];
    
    return self;
}

-(void)onClickDeleteButton:(UIButton*) button
{
    [aryPicture removeObjectAtIndex:button.tag];
    
    [self reloadPictureData];
}

-(void)reloadPictureData
{
    for (UIView *view in self.subviews)
    {
        [view removeFromSuperview];
    }
    
    for (int i = 0; i < aryPicture.count; i++) {
        UIView *view = [[UIView alloc] initWithFrame:CGRectMake(i * 90, 0, 90, 82)];
        
        UIImage *img = [aryPicture objectAtIndex:i];
        UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(2, 2, 80, 80)];
        [imgView setImage:img];
        
        [view addSubview:imgView];
        
        UIButton *btnDel = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 17, 17)];
        btnDel.tag = i;
        [btnDel setImage:[UIImage imageNamed:@"fabu_delete"] forState:UIControlStateNormal];
        [btnDel addTarget:self action:@selector(onClickDeleteButton:) forControlEvents:UIControlEventTouchUpInside];
        
        [view addSubview:btnDel];
        
        [self addSubview:view];
    }
    
    UIView *viewAdd = [[UIView alloc] initWithFrame:CGRectMake(aryPicture.count * 90, 0, 90, 82)];
    UIButton *btnPicture = [[UIButton alloc] initWithFrame:CGRectMake(2, 2, 80, 80)];
    [btnPicture addTarget:self action:@selector(onClickAddPicture:) forControlEvents:UIControlEventTouchUpInside];
    [btnPicture setBackgroundColor:BLACK_COLOR_245];
    [viewAdd addSubview:btnPicture];
    
    UIButton *btnAdd = [[UIButton alloc] initWithFrame:CGRectMake(33, 24, 19, 19)];
    [btnAdd setBackgroundImage:[UIImage imageNamed:@"fabu_add_small"] forState:UIControlStateNormal];
    [btnAdd addTarget:self action:@selector(onClickAddPicture:) forControlEvents:UIControlEventTouchUpInside];
    [viewAdd addSubview:btnAdd];
    
    UIButton *btnText = [[UIButton alloc] initWithFrame:CGRectMake(2, 48, 80, 15)];
    [btnText setTitle:strAddPic forState:UIControlStateNormal];
    [btnText.titleLabel setFont:FONT_12];
    
    [btnText setTitleColor:BLACK_COLOR_102 forState:UIControlStateNormal];
    [btnText addTarget:self action:@selector(onClickAddPicture:) forControlEvents:UIControlEventTouchUpInside];
    [viewAdd addSubview:btnText];
    [self addSubview:viewAdd];
    
    [self setContentSize:CGSizeMake((aryPicture.count + 1) * 90, 82)];

}

-(void)onClickAddPicture:(UIButton*) button
{
    
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.

- (void)drawRect:(CGRect)rect {
    // Drawing code
}
 */


@end
