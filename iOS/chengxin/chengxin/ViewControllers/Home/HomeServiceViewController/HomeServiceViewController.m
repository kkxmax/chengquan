//
//  HomeServiceViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeServiceViewController.h"
#import "HomeServiceTableViewCell.h"
#import "Global.h"

@interface HomeServiceViewController ()

@end

@implementation HomeServiceViewController
@synthesize slideServiceScrollView, slideServicePageCtrl, homeServiceTableView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.homeServiceTableView registerNib:[UINib nibWithNibName:@"HomeServiceTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeServiceCellIdentifier"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // Set slide page time
    [self showSlideShow];
    slideServicePageTimer = [NSTimer scheduledTimerWithTimeInterval:SLIDE_SECOND target:self selector:@selector(PageMove) userInfo:nil repeats:YES];
}

#pragma mark - Page Slide
- (void)showSlideShow {
    slideServiceScrollView.contentSize = CGSizeMake(slideServiceScrollView.frame.size.width * NUMBER_OF_SLIDE_IMAGES, slideServiceScrollView.frame.size.height);
    CGRect ViewSize = slideServiceScrollView.bounds;
    for(int i = 0; i <= NUMBER_OF_SLIDE_IMAGES; i++)
    {
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:ViewSize];
        imageView.contentMode = UIViewContentModeScaleToFill;
        [imageView setImage:[UIImage imageNamed:[NSString stringWithFormat:@"%d.png", (SERVICE_IMAGE_START + i)]]];
        [slideServiceScrollView addSubview:imageView];
        ViewSize = CGRectOffset(ViewSize, slideServiceScrollView.bounds.size.width, 0);
    }
}
- (void)PageMove {
    CGFloat pageSize = slideServiceScrollView.frame.size.width;
    NSInteger nCurrentPage = 0;
    // if this is the last page return
    if(slideServiceScrollView.contentOffset.x >= slideServiceScrollView.frame.size.width * NUMBER_OF_SLIDE_IMAGES) {
        [slideServiceScrollView setContentOffset:CGPointMake(0.0, slideServiceScrollView.contentOffset.y) animated:YES];
    } else {
        [slideServiceScrollView setContentOffset:CGPointMake(slideServiceScrollView.contentOffset.x + pageSize, slideServiceScrollView.contentOffset.y) animated:YES];
        nCurrentPage = (NSInteger)(slideServiceScrollView.contentOffset.x / slideServiceScrollView.frame.size.width) + 1;
    }
    [slideServicePageCtrl setCurrentPage:nCurrentPage];
}

#pragma mark - TableView
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeServiceTableViewCell *homeServiceTableCell = (HomeServiceTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeServiceCellIdentifier" forIndexPath:indexPath];
    return homeServiceTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 5;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 155.f;
    return homeTableCellHeight;
}

#pragma mark - Action
- (IBAction)addAction:(id)sender {
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
